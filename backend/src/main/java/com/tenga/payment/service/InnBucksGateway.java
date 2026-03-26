package com.tenga.payment.service;

import com.tenga.payment.model.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Qualifier("innBucksGateway")
@ConditionalOnProperty(
    name = "tenga.payment.innbucks.mock",
    havingValue = "false",
    matchIfMissing = true)
public class InnBucksGateway implements PaymentGateway {

  private static final Logger log = LoggerFactory.getLogger(InnBucksGateway.class);

  private final RestClient restClient;
  private final String apiUrl;
  private final String apiKey;
  private final String callbackUrl;

  public InnBucksGateway(
      @Value("${tenga.payment.innbucks.api-url}") String apiUrl,
      @Value("${tenga.payment.innbucks.api-key}") String apiKey,
      @Value("${tenga.payment.innbucks.callback-url}") String callbackUrl) {
    this.restClient = RestClient.create();
    this.apiUrl = apiUrl;
    this.apiKey = apiKey;
    this.callbackUrl = callbackUrl;
  }

  @Override
  public String initiate(Transaction transaction) {
    String requestBody =
        """
        {
          "amount": %s,
          "currency": "%s",
          "reference": "%s",
          "callbackUrl": "%s",
          "payerPhone": "%s"
        }
        """
            .formatted(
                transaction.getAmount().toPlainString(),
                transaction.getCurrency().name(),
                transaction.getId(),
                callbackUrl,
                transaction.getPayerPhone());

    String rawResponse =
        restClient
            .post()
            .uri(apiUrl + "/payments/initiate")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + apiKey)
            .body(requestBody)
            .retrieve()
            .body(String.class);

    log.info(
        "InnBucks initiation response for txId={}: {}",
        transaction.getId(),
        rawResponse != null
            ? rawResponse.substring(0, Math.min(rawResponse.length(), 200))
            : "null");

    return transaction.getId().toString();
  }
}
