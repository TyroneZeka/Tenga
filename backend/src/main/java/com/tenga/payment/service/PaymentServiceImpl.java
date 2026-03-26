package com.tenga.payment.service;

import com.tenga.listing.model.enums.Currency;
import com.tenga.payment.exception.PaymentNotFoundException;
import com.tenga.payment.model.dto.InitiatePaymentRequest;
import com.tenga.payment.model.dto.PaymentCallbackPayload;
import com.tenga.payment.model.dto.TransactionResponse;
import com.tenga.payment.model.dto.WalletResponse;
import com.tenga.payment.model.entity.Transaction;
import com.tenga.payment.model.entity.Wallet;
import com.tenga.payment.model.enums.PaymentMethod;
import com.tenga.payment.model.mapper.PaymentMapper;
import com.tenga.payment.repository.TransactionRepository;
import com.tenga.payment.repository.WalletRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

  private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;
  private final PaymentMapper paymentMapper;
  private final Map<PaymentMethod, PaymentGateway> gateways;

  public PaymentServiceImpl(
      TransactionRepository transactionRepository,
      WalletRepository walletRepository,
      PaymentMapper paymentMapper,
      @Qualifier("ecoCashGateway") PaymentGateway ecoCashGateway,
      @Qualifier("innBucksGateway") PaymentGateway innBucksGateway) {
    this.transactionRepository = transactionRepository;
    this.walletRepository = walletRepository;
    this.paymentMapper = paymentMapper;
    this.gateways =
        Map.of(PaymentMethod.ECOCASH, ecoCashGateway, PaymentMethod.INNBUCKS, innBucksGateway);
  }

  @Override
  @Transactional
  public TransactionResponse initiatePayment(
      UUID buyerId, PaymentMethod method, InitiatePaymentRequest request) {
    Transaction tx =
        new Transaction(
            buyerId,
            request.sellerId(),
            request.listingId(),
            request.amount(),
            request.currency(),
            method,
            request.payerPhone());
    tx = transactionRepository.save(tx);

    try {
      String ref = gateways.get(method).initiate(tx);
      tx.markProcessing(ref);
      transactionRepository.save(tx);
      log.info("Payment initiated: txId={}, method={}, buyerId={}", tx.getId(), method, buyerId);
    } catch (Exception e) {
      tx.markFailed("Gateway error: " + e.getMessage());
      transactionRepository.save(tx);
      log.error("Payment initiation failed: txId={}, error={}", tx.getId(), e.getMessage(), e);
    }

    return paymentMapper.toResponse(tx);
  }

  @Override
  @Transactional
  public void handleCallback(PaymentCallbackPayload payload) {
    Transaction tx =
        transactionRepository
            .findByGatewayReference(payload.gatewayReference())
            .orElseThrow(() -> new PaymentNotFoundException(null));

    if ("SUCCESS".equalsIgnoreCase(payload.status())) {
      tx.markCompleted();
      // Credit seller's wallet
      Wallet sellerWallet = getOrCreateWallet(tx.getSellerId(), tx.getCurrency());
      sellerWallet.credit(tx.getAmount());
      walletRepository.save(sellerWallet);
      log.info(
          "Payment completed: txId={}, sellerId={}, amount={} {}",
          tx.getId(),
          tx.getSellerId(),
          tx.getAmount(),
          tx.getCurrency());
    } else {
      tx.markFailed(payload.failureReason());
      log.warn(
          "Payment failed via callback: txId={}, reason={}", tx.getId(), payload.failureReason());
    }

    transactionRepository.save(tx);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TransactionResponse> getTransactions(UUID userId, Pageable pageable) {
    return transactionRepository
        .findByBuyerIdOrSellerIdOrderByCreatedAtDesc(userId, userId, pageable)
        .map(paymentMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public List<WalletResponse> getWallets(UUID userId) {
    return walletRepository.findByUserId(userId).stream().map(paymentMapper::toResponse).toList();
  }

  private Wallet getOrCreateWallet(UUID userId, Currency currency) {
    return walletRepository
        .findByUserIdAndCurrency(userId, currency)
        .orElseGet(() -> walletRepository.save(new Wallet(userId, currency)));
  }
}
