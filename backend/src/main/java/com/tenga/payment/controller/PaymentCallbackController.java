package com.tenga.payment.controller;

import com.tenga.payment.model.dto.PaymentCallbackPayload;
import com.tenga.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Receives payment status callbacks from EcoCash and InnBucks. These endpoints must be publicly
 * accessible (no JWT) — they are protected by IP allowlist and signature verification at the
 * reverse-proxy / gateway level.
 */
@RestController
@RequestMapping("/api/v1/payments/callbacks")
@Tag(name = "Payment Callbacks", description = "Webhook endpoints for gateway callbacks")
public class PaymentCallbackController {

  private final PaymentService paymentService;

  public PaymentCallbackController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/ecocash")
  @Operation(summary = "EcoCash payment result callback")
  public ResponseEntity<Void> ecoCashCallback(@RequestBody PaymentCallbackPayload payload) {
    paymentService.handleCallback(payload);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/innbucks")
  @Operation(summary = "InnBucks payment result callback")
  public ResponseEntity<Void> innBucksCallback(@RequestBody PaymentCallbackPayload payload) {
    paymentService.handleCallback(payload);
    return ResponseEntity.ok().build();
  }
}
