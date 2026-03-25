package com.tenga.payment.controller;

import com.tenga.payment.model.dto.InitiatePaymentRequest;
import com.tenga.payment.model.dto.TransactionResponse;
import com.tenga.payment.model.dto.WalletResponse;
import com.tenga.payment.model.enums.PaymentMethod;
import com.tenga.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@PreAuthorize("isAuthenticated()")
@Tag(
    name = "Payments",
    description = "EcoCash and InnBucks payment initiation and transaction history")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/ecocash")
  @Operation(summary = "Initiate an EcoCash USSD push payment")
  public ResponseEntity<TransactionResponse> payWithEcoCash(
      @AuthenticationPrincipal UUID userId, @Valid @RequestBody InitiatePaymentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(paymentService.initiatePayment(userId, PaymentMethod.ECOCASH, request));
  }

  @PostMapping("/innbucks")
  @Operation(summary = "Initiate an InnBucks app payment")
  public ResponseEntity<TransactionResponse> payWithInnBucks(
      @AuthenticationPrincipal UUID userId, @Valid @RequestBody InitiatePaymentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(paymentService.initiatePayment(userId, PaymentMethod.INNBUCKS, request));
  }

  @GetMapping("/transactions")
  @Operation(summary = "List transactions for the current user")
  public ResponseEntity<Page<TransactionResponse>> getTransactions(
      @AuthenticationPrincipal UUID userId, @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(paymentService.getTransactions(userId, pageable));
  }

  @GetMapping("/wallets")
  @Operation(summary = "Get wallet balances for the current user")
  public ResponseEntity<List<WalletResponse>> getWallets(@AuthenticationPrincipal UUID userId) {
    return ResponseEntity.ok(paymentService.getWallets(userId));
  }
}
