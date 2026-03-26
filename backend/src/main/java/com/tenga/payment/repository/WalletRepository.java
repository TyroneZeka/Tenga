package com.tenga.payment.repository;

import com.tenga.listing.model.enums.Currency;
import com.tenga.payment.model.entity.Wallet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

  Optional<Wallet> findByUserIdAndCurrency(UUID userId, Currency currency);

  List<Wallet> findByUserId(UUID userId);
}
