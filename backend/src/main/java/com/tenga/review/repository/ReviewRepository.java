package com.tenga.review.repository;

import com.tenga.review.model.entity.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

  Page<Review> findByRevieweeIdOrderByCreatedAtDesc(UUID revieweeId, Pageable pageable);

  Optional<Review> findByTransactionId(UUID transactionId);

  @Query(
      "SELECT COALESCE(AVG(CAST(r.rating AS double)), 0.0) FROM Review r WHERE r.revieweeId = :revieweeId")
  double computeAverageRating(UUID revieweeId);
}
