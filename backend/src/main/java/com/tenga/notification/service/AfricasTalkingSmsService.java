package com.tenga.notification.service;

import com.tenga.notification.exception.NotificationDeliveryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

/**
 * SMS delivery via Africa's Talking — primary SMS gateway for Zimbabwe.
 *
 * <p>API docs: https://developers.africastalking.com/docs/sms/sending
 */
@Service
@ConditionalOnProperty(
    name = "tenga.notification.sms-provider",
    havingValue = "africas-talking",
    matchIfMissing = true)
public class AfricasTalkingSmsService implements SmsService {

  private static final Logger log = LoggerFactory.getLogger(AfricasTalkingSmsService.class);
  private static final String AT_API_URL = "https://api.africastalking.com/version1/messaging";

  private final RestClient restClient;
  private final String apiKey;
  private final String username;
  private final String senderId;

  public AfricasTalkingSmsService(
      RestClient.Builder restClientBuilder,
      @Value("${tenga.notification.africas-talking.api-key}") String apiKey,
      @Value("${tenga.notification.africas-talking.username}") String username,
      @Value("${tenga.notification.africas-talking.sender-id:TENGA}") String senderId) {
    this.restClient = restClientBuilder.build();
    this.apiKey = apiKey;
    this.username = username;
    this.senderId = senderId;
  }

  @Override
  public void send(String phoneNumber, String message) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("username", username);
    body.add("to", phoneNumber);
    body.add("message", message);
    body.add("from", senderId);

    try {
      var response =
          restClient
              .post()
              .uri(AT_API_URL)
              .header("apiKey", apiKey)
              .header("Accept", MediaType.APPLICATION_JSON_VALUE)
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .body(body)
              .retrieve()
              .toBodilessEntity();

      if (!response.getStatusCode().is2xxSuccessful()) {
        throw new NotificationDeliveryException(
            "Africa's Talking returned HTTP " + response.getStatusCode() + " for " + phoneNumber);
      }
      log.info("SMS sent via Africa's Talking: to={}", phoneNumber);
    } catch (NotificationDeliveryException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new NotificationDeliveryException(
          "SMS delivery failed for " + phoneNumber + ": " + ex.getMessage(), ex);
    }
  }
}
