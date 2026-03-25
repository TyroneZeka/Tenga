package com.tenga.payment.service;

import com.tenga.payment.model.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@Qualifier("ecoCashGateway")
@ConditionalOnProperty(
    name = "tenga.payment.ecocash.mock",
    havingValue = "false",
    matchIfMissing = true)
public class EcoCashGateway implements PaymentGateway {

  private static final Logger log = LoggerFactory.getLogger(EcoCashGateway.class);

  private final RestClient restClient;
  private final String apiUrl;
  private final String merchantCode;
  private final String pin;
  private final String callbackUrl;

  public EcoCashGateway(
      @Value("${tenga.payment.ecocash.api-url}") String apiUrl,
      @Value("${tenga.payment.ecocash.merchant-code}") String merchantCode,
      @Value("${tenga.payment.ecocash.pin}") String pin,
      @Value("${tenga.payment.ecocash.callback-url}") String callbackUrl) {
    this.restClient = RestClient.create();
    this.apiUrl = apiUrl;
    this.merchantCode = merchantCode;
    this.pin = pin;
    this.callbackUrl = callbackUrl;
  }

  @Override
  public String initiate(Transaction transaction) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("merchantCode", merchantCode);
    body.add("merchantPin", pin);
    body.add("merchantNumber", merchantCode);
    body.add("transactionRef", transaction.getId().toString());
    body.add("paymentAmount", transaction.getAmount().toPlainString());
    body.add("clientCorrelator", transaction.getId().toString());
    body.add("notifyUrl", callbackUrl);
    body.add("msisdn", transaction.getPayerPhone());
    body.add("transactionOperationStatus", "Charged");

    // EcoCash returns a reference in the response body; real parsing would be done here
    String rawResponse =
        restClient
            .post()
            .uri(apiUrl + "/transactions/amount")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(String.class);

    log.info(
        "EcoCash initiation response for txId={}: {}",
        transaction.getId(),
        rawResponse != null
            ? rawResponse.substring(0, Math.min(rawResponse.length(), 200))
            : "null");

    // Use our own transaction ID as the reference until a real parser is wired up
    return transaction.getId().toString();
  }
}
