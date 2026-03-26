package com.tenga.config;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a test user on dev startup so the frontend can be exercised without going through the
 * SMS/email OTP flow.
 *
 * <pre>
 *   Phone: +263771234567
 *   Password: Test1234!
 * </pre>
 */
@Order(1)
@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DevDataInitializer.class);

  private static final String TEST_PHONE = "+263771234567";
  private static final String TEST_PASSWORD = "Test1234!";
  private static final String TEST_DISPLAY_NAME = "Test User";

  private final JdbcTemplate jdbc;
  private final PasswordEncoder passwordEncoder;

  public DevDataInitializer(JdbcTemplate jdbc, PasswordEncoder passwordEncoder) {
    this.jdbc = jdbc;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    createTestUserIfAbsent();
  }

  private void createTestUserIfAbsent() {
    Integer count =
        jdbc.queryForObject(
            "SELECT COUNT(*) FROM auth_users WHERE phone_number = ?", Integer.class, TEST_PHONE);

    if (count != null && count > 0) {
      log.info("[DEV] Test user already exists — skipping seed");
      return;
    }

    UUID userId = UUID.randomUUID();
    String passwordHash = passwordEncoder.encode(TEST_PASSWORD);

    jdbc.update(
        """
        INSERT INTO auth_users (
            id, phone_number, password_hash, role, provider,
            phone_verified, email_verified, enabled,
            created_at, updated_at, version
        ) VALUES (?, ?, ?, 'SELLER', 'LOCAL', true, false, true, now(), now(), 0)
        """,
        userId,
        TEST_PHONE,
        passwordHash);

    jdbc.update(
        """
        INSERT INTO usr_profiles (
            id, user_id, display_name,
            created_at, updated_at, version
        ) VALUES (?, ?, ?, now(), now(), 0)
        """,
        UUID.randomUUID(),
        userId,
        TEST_DISPLAY_NAME);

    log.info("[DEV] Test user created — phone={}, password={}", TEST_PHONE, TEST_PASSWORD);
  }
}
