package com.tenga.auth.service;

import com.tenga.auth.model.entity.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final String CLAIM_ROLE = "role";
  private static final String CLAIM_USER_ID = "uid";

  private final SecretKey signingKey;
  private final long accessTokenExpirySeconds;

  public JwtService(
      @Value("${tenga.jwt.secret}") String secret,
      @Value("${tenga.jwt.access-token-expiry}") long accessTokenExpirySeconds) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpirySeconds = accessTokenExpirySeconds;
  }

  public String generateAccessToken(AuthUser user) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(user.getId().toString())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(accessTokenExpirySeconds)))
        .claims(Map.of(CLAIM_ROLE, user.getRole().name(), CLAIM_USER_ID, user.getId().toString()))
        .signWith(signingKey)
        .compact();
  }

  public Claims validateAndParse(String token) throws JwtException {
    return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
  }

  public String extractSubject(String token) {
    return validateAndParse(token).getSubject();
  }

  public long getAccessTokenExpirySeconds() {
    return accessTokenExpirySeconds;
  }
}
