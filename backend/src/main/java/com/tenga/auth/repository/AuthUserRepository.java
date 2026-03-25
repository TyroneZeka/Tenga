package com.tenga.auth.repository;

import com.tenga.auth.model.entity.AuthUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

  Optional<AuthUser> findByEmail(String email);

  Optional<AuthUser> findByPhoneNumber(String phoneNumber);

  @Query(
      "SELECT u FROM AuthUser u WHERE (u.email = :username OR u.phoneNumber = :username) AND u.deletedAt IS NULL AND u.enabled = true")
  Optional<AuthUser> findActiveByEmailOrPhone(String username);

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);
}
