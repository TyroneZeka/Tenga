package com.tenga.review.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "rev_reviews")
public class Review extends BaseEntity {

  /** The user who received this review (typically the seller). */
  @Column(name = "reviewee_id", nullable = false)
  private UUID revieweeId;

  /** The user who wrote this review (typically the buyer). */
  @Column(name = "reviewer_id", nullable = false)
  private UUID reviewerId;

  /** The completed transaction this review is associated with. */
  @Column(name = "transaction_id", nullable = false, unique = true)
  private UUID transactionId;

  @Column(name = "listing_id", nullable = false)
  private UUID listingId;

  /** Rating from 1 to 5. */
  @Column(name = "rating", nullable = false)
  private int rating;

  @Column(name = "comment", length = 1000)
  private String comment;

  protected Review() {}

  public Review(
      UUID revieweeId,
      UUID reviewerId,
      UUID transactionId,
      UUID listingId,
      int rating,
      String comment) {
    if (rating < 1 || rating > 5) {
      throw new IllegalArgumentException("Rating must be between 1 and 5");
    }
    this.revieweeId = revieweeId;
    this.reviewerId = reviewerId;
    this.transactionId = transactionId;
    this.listingId = listingId;
    this.rating = rating;
    this.comment = comment;
  }

  public UUID getRevieweeId() {
    return revieweeId;
  }

  public UUID getReviewerId() {
    return reviewerId;
  }

  public UUID getTransactionId() {
    return transactionId;
  }

  public UUID getListingId() {
    return listingId;
  }

  public int getRating() {
    return rating;
  }

  public String getComment() {
    return comment;
  }
}
