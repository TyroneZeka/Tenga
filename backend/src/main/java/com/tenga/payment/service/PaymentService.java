package com.tenga.payment.service;

import com.tenga.payment.model.dto.InitiatePaymentRequest;
import com.tenga.payment.model.dto.PaymentCallbackPayload;
import com.tenga.payment.model.dto.TransactionResponse;
import com.tenga.payment.model.dto.WalletResponse;
import com.tenga.payment.model.enums.PaymentMethod;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

  TransactionResponse initiatePayment(
      UUID buyerId, PaymentMethod method, InitiatePaymentRequest request);

  void handleCallback(PaymentCallbackPayload payload);

  Page<TransactionResponse> getTransactions(UUID userId, Pageable pageable);

  List<WalletResponse> getWallets(UUID userId);
}
