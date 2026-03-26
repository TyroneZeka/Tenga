package com.tenga.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.listing.model.enums.Currency;
import com.tenga.payment.exception.PaymentNotFoundException;
import com.tenga.payment.model.dto.InitiatePaymentRequest;
import com.tenga.payment.model.dto.PaymentCallbackPayload;
import com.tenga.payment.model.dto.TransactionResponse;
import com.tenga.payment.model.entity.Transaction;
import com.tenga.payment.model.entity.Wallet;
import com.tenga.payment.model.enums.PaymentMethod;
import com.tenga.payment.model.enums.TransactionStatus;
import com.tenga.payment.model.mapper.PaymentMapperImpl;
import com.tenga.payment.repository.TransactionRepository;
import com.tenga.payment.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

  @Mock private TransactionRepository transactionRepository;
  @Mock private WalletRepository walletRepository;
  @Mock private PaymentGateway ecoCashGateway;
  @Mock private PaymentGateway innBucksGateway;
  @Spy private PaymentMapperImpl paymentMapper;

  private PaymentServiceImpl paymentService;

  @BeforeEach
  void setUp() {
    paymentService =
        new PaymentServiceImpl(
            transactionRepository, walletRepository, paymentMapper, ecoCashGateway, innBucksGateway);
  }

  // ── initiatePayment ────────────────────────────────────────────────────────

  @Test
  void should_initiateEcoCashPayment_and_returnProcessingStatus() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    InitiatePaymentRequest request =
        new InitiatePaymentRequest(
            listingId, sellerId, new BigDecimal("15.00"), Currency.USD, "+263771234567");

    Transaction savedTx =
        new Transaction(
            buyerId,
            sellerId,
            listingId,
            new BigDecimal("15.00"),
            Currency.USD,
            PaymentMethod.ECOCASH,
            "+263771234567");
    ReflectionTestUtils.setField(savedTx, "id", UUID.randomUUID());

    when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTx);
    when(ecoCashGateway.initiate(any(Transaction.class))).thenReturn("ECO-REF-001");

    TransactionResponse response =
        paymentService.initiatePayment(buyerId, PaymentMethod.ECOCASH, request);

    assertThat(response.paymentMethod()).isEqualTo(PaymentMethod.ECOCASH);
    assertThat(response.buyerId()).isEqualTo(buyerId);
    verify(ecoCashGateway).initiate(any(Transaction.class));
  }

  @Test
  void should_markTransactionFailed_when_gatewayThrowsException() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    InitiatePaymentRequest request =
        new InitiatePaymentRequest(
            listingId, sellerId, new BigDecimal("450.00"), Currency.ZIG, "+263711234567");

    Transaction savedTx =
        new Transaction(
            buyerId,
            sellerId,
            listingId,
            new BigDecimal("450.00"),
            Currency.ZIG,
            PaymentMethod.ECOCASH,
            "+263711234567");
    ReflectionTestUtils.setField(savedTx, "id", UUID.randomUUID());

    when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTx);
    when(ecoCashGateway.initiate(any(Transaction.class)))
        .thenThrow(new RuntimeException("Gateway timeout"));

    // Should not propagate — the service catches the exception and marks failed
    TransactionResponse response =
        paymentService.initiatePayment(buyerId, PaymentMethod.ECOCASH, request);

    assertThat(response).isNotNull();
    // save is called twice: initial save + failed save
    verify(transactionRepository, org.mockito.Mockito.times(2)).save(any(Transaction.class));
  }

  @Test
  void should_initiateInnBucksPayment_using_correct_gateway() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    InitiatePaymentRequest request =
        new InitiatePaymentRequest(
            listingId, sellerId, new BigDecimal("20.00"), Currency.USD, "+263731234567");

    Transaction savedTx =
        new Transaction(
            buyerId,
            sellerId,
            listingId,
            new BigDecimal("20.00"),
            Currency.USD,
            PaymentMethod.INNBUCKS,
            "+263731234567");
    ReflectionTestUtils.setField(savedTx, "id", UUID.randomUUID());

    when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTx);
    when(innBucksGateway.initiate(any(Transaction.class))).thenReturn("INN-REF-042");

    paymentService.initiatePayment(buyerId, PaymentMethod.INNBUCKS, request);

    verify(innBucksGateway).initiate(any(Transaction.class));
    verify(ecoCashGateway, never()).initiate(any());
  }

  // ── handleCallback ─────────────────────────────────────────────────────────

  @Test
  void should_markCompleted_and_creditSellerWallet_on_successCallback() {
    UUID sellerId = UUID.randomUUID();
    UUID buyerId = UUID.randomUUID();
    String gatewayRef = "ECO-REF-SUCCESS";

    Transaction tx =
        new Transaction(
            buyerId,
            sellerId,
            UUID.randomUUID(),
            new BigDecimal("15.00"),
            Currency.USD,
            PaymentMethod.ECOCASH,
            "+263771234567");
    ReflectionTestUtils.setField(tx, "id", UUID.randomUUID());
    tx.markProcessing(gatewayRef);

    Wallet sellerWallet = new Wallet(sellerId, Currency.USD);

    PaymentCallbackPayload payload = new PaymentCallbackPayload(gatewayRef, "SUCCESS", null);

    when(transactionRepository.findByGatewayReference(gatewayRef)).thenReturn(Optional.of(tx));
    when(walletRepository.findByUserIdAndCurrency(sellerId, Currency.USD))
        .thenReturn(Optional.of(sellerWallet));
    when(walletRepository.save(any(Wallet.class))).thenReturn(sellerWallet);
    when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);

    paymentService.handleCallback(payload);

    assertThat(tx.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    assertThat(sellerWallet.getBalance()).isEqualByComparingTo("15.00");
    verify(walletRepository).save(sellerWallet);
  }

  @Test
  void should_createNewWallet_when_sellerHasNoWallet_on_successCallback() {
    UUID sellerId = UUID.randomUUID();
    UUID buyerId = UUID.randomUUID();
    String gatewayRef = "ECO-REF-NEW-WALLET";

    Transaction tx =
        new Transaction(
            buyerId,
            sellerId,
            UUID.randomUUID(),
            new BigDecimal("30.00"),
            Currency.USD,
            PaymentMethod.ECOCASH,
            "+263771234567");
    ReflectionTestUtils.setField(tx, "id", UUID.randomUUID());
    tx.markProcessing(gatewayRef);

    Wallet newWallet = new Wallet(sellerId, Currency.USD);

    PaymentCallbackPayload payload = new PaymentCallbackPayload(gatewayRef, "SUCCESS", null);

    when(transactionRepository.findByGatewayReference(gatewayRef)).thenReturn(Optional.of(tx));
    when(walletRepository.findByUserIdAndCurrency(sellerId, Currency.USD))
        .thenReturn(Optional.empty());
    when(walletRepository.save(any(Wallet.class))).thenReturn(newWallet);
    when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);

    paymentService.handleCallback(payload);

    // wallet is saved twice: once to create it, once to persist the credited balance
    verify(walletRepository, org.mockito.Mockito.times(2)).save(any(Wallet.class));
  }

  @Test
  void should_markFailed_when_callbackStatusIsNotSuccess() {
    UUID sellerId = UUID.randomUUID();
    UUID buyerId = UUID.randomUUID();
    String gatewayRef = "ECO-REF-FAIL";

    Transaction tx =
        new Transaction(
            buyerId,
            sellerId,
            UUID.randomUUID(),
            new BigDecimal("15.00"),
            Currency.USD,
            PaymentMethod.ECOCASH,
            "+263771234567");
    ReflectionTestUtils.setField(tx, "id", UUID.randomUUID());
    tx.markProcessing(gatewayRef);

    PaymentCallbackPayload payload =
        new PaymentCallbackPayload(gatewayRef, "FAILED", "Insufficient funds");

    when(transactionRepository.findByGatewayReference(gatewayRef)).thenReturn(Optional.of(tx));
    when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);

    paymentService.handleCallback(payload);

    assertThat(tx.getStatus()).isEqualTo(TransactionStatus.FAILED);
    assertThat(tx.getFailureReason()).isEqualTo("Insufficient funds");
    verify(walletRepository, never()).save(any());
  }

  @Test
  void should_throwPaymentNotFoundException_when_gatewayReferenceIsUnknown() {
    PaymentCallbackPayload payload =
        new PaymentCallbackPayload("UNKNOWN-REF", "SUCCESS", null);

    when(transactionRepository.findByGatewayReference("UNKNOWN-REF"))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> paymentService.handleCallback(payload))
        .isInstanceOf(PaymentNotFoundException.class);
  }

  // ── getWallets ─────────────────────────────────────────────────────────────

  @Test
  void should_returnAllWalletsForUser() {
    UUID userId = UUID.randomUUID();

    Wallet usdWallet = new Wallet(userId, Currency.USD);
    Wallet zigWallet = new Wallet(userId, Currency.ZIG);

    when(walletRepository.findByUserId(userId)).thenReturn(List.of(usdWallet, zigWallet));

    assertThat(paymentService.getWallets(userId)).hasSize(2);
  }
}
