package com.tenga.payment.model.dto;

/** Generic callback payload — both EcoCash and InnBucks are normalised to this shape. */
public record PaymentCallbackPayload(
    String gatewayReference, String status, String failureReason) {}
