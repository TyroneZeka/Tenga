package com.tenga.chat.repository;

import com.tenga.chat.model.entity.ChatThread;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatThreadRepository extends JpaRepository<ChatThread, UUID> {

  @Query(
      "SELECT t FROM ChatThread t WHERE (t.buyerId = :userId OR t.sellerId = :userId) ORDER BY t.updatedAt DESC")
  List<ChatThread> findByParticipant(UUID userId);

  Optional<ChatThread> findByBuyerIdAndSellerIdAndListingId(
      UUID buyerId, UUID sellerId, UUID listingId);
}
