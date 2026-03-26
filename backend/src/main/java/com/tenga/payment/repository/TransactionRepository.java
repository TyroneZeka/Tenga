package com.tenga.payment.repository;

import com.tenga.payment.model.entity.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  Page<Transaction> findByBuyerIdOrSellerIdOrderByCreatedAtDesc(
      UUID buyerId, UUID sellerId, Pageable pageable);

  Optional<Transaction> findByGatewayReference(String gatewayReference);

  List<Transaction> findByListingIdAndStatusIn(
      UUID listingId, List<com.tenga.payment.model.enums.TransactionStatus> statuses);
}
