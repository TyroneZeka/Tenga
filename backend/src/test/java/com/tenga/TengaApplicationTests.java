package com.tenga;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource(
    properties = {
      "tenga.jwt.secret=test-secret-key-must-be-at-least-256-bits-long-here",
      "tenga.s3.bucket=tenga-test",
      "tenga.s3.access-key=test",
      "tenga.s3.secret-key=test",
      "tenga.s3.endpoint=http://localhost:9000",
    })
class TengaApplicationTests {

  @Test
  void contextLoads() {
    // Verifies the Spring application context starts without errors
  }
}
