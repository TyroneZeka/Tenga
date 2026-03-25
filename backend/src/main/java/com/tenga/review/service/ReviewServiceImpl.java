package com.tenga.review.service;

import com.tenga.review.exception.DuplicateReviewException;
import com.tenga.review.model.dto.CreateReviewRequest;
import com.tenga.review.model.dto.ReviewResponse;
import com.tenga.review.model.entity.Review;
import com.tenga.review.model.mapper.ReviewMapper;
import com.tenga.review.repository.ReviewRepository;
import com.tenga.user.repository.UserProfileRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

  private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;
  private final UserProfileRepository userProfileRepository;

  public ReviewServiceImpl(
      ReviewRepository reviewRepository,
      ReviewMapper reviewMapper,
      UserProfileRepository userProfileRepository) {
    this.reviewRepository = reviewRepository;
    this.reviewMapper = reviewMapper;
    this.userProfileRepository = userProfileRepository;
  }

  @Override
  @Transactional
  public ReviewResponse createReview(UUID reviewerId, CreateReviewRequest request) {
    if (reviewRepository.findByTransactionId(request.transactionId()).isPresent()) {
      throw new DuplicateReviewException();
    }

    Review review =
        new Review(
            request.revieweeId(),
            reviewerId,
            request.transactionId(),
            request.listingId(),
            request.rating(),
            request.comment());
    review = reviewRepository.save(review);

    updateTrustScore(request.revieweeId());

    log.info(
        "Review created: id={}, revieweeId={}, rating={}",
        review.getId(),
        request.revieweeId(),
        request.rating());
    return reviewMapper.toResponse(review);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReviewResponse> getReviewsForUser(UUID revieweeId, Pageable pageable) {
    return reviewRepository
        .findByRevieweeIdOrderByCreatedAtDesc(revieweeId, pageable)
        .map(reviewMapper::toResponse);
  }

  private void updateTrustScore(UUID revieweeId) {
    double avg = reviewRepository.computeAverageRating(revieweeId);
    BigDecimal trustScore = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
    userProfileRepository
        .findByUserId(revieweeId)
        .ifPresent(
            profile -> {
              profile.updateTrustScore(trustScore);
              userProfileRepository.save(profile);
              log.info("Trust score updated: userId={}, score={}", revieweeId, trustScore);
            });
  }
}
