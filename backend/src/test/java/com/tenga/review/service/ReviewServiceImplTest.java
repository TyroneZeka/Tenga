package com.tenga.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.review.exception.DuplicateReviewException;
import com.tenga.review.model.dto.CreateReviewRequest;
import com.tenga.review.model.dto.ReviewResponse;
import com.tenga.review.model.entity.Review;
import com.tenga.review.model.mapper.ReviewMapperImpl;
import com.tenga.review.repository.ReviewRepository;
import com.tenga.user.model.entity.UserProfile;
import com.tenga.user.repository.UserProfileRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @Mock private ReviewRepository reviewRepository;
  @Mock private UserProfileRepository userProfileRepository;
  @Spy private ReviewMapperImpl reviewMapper;

  @InjectMocks private ReviewServiceImpl reviewService;

  // ── createReview ───────────────────────────────────────────────────────────

  @Test
  void should_createReview_and_updateTrustScore_when_requestIsValid() {
    UUID reviewerId = UUID.randomUUID();
    UUID revieweeId = UUID.randomUUID();
    UUID transactionId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    CreateReviewRequest request =
        new CreateReviewRequest(transactionId, revieweeId, listingId, 5, "Great seller!");

    Review savedReview =
        new Review(revieweeId, reviewerId, transactionId, listingId, 5, "Great seller!");
    ReflectionTestUtils.setField(savedReview, "id", UUID.randomUUID());

    UserProfile profile = new UserProfile(revieweeId);

    when(reviewRepository.findByTransactionId(transactionId)).thenReturn(Optional.empty());
    when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
    when(reviewRepository.computeAverageRating(revieweeId)).thenReturn(4.8);
    when(userProfileRepository.findByUserId(revieweeId)).thenReturn(Optional.of(profile));
    when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);

    ReviewResponse response = reviewService.createReview(reviewerId, request);

    assertThat(response.revieweeId()).isEqualTo(revieweeId);
    assertThat(response.reviewerId()).isEqualTo(reviewerId);
    assertThat(response.rating()).isEqualTo(5);
    verify(userProfileRepository).save(profile);
  }

  @Test
  void should_throwDuplicateReviewException_when_transactionAlreadyReviewed() {
    UUID transactionId = UUID.randomUUID();
    UUID revieweeId = UUID.randomUUID();

    CreateReviewRequest request =
        new CreateReviewRequest(transactionId, revieweeId, UUID.randomUUID(), 4, "Good");

    Review existing =
        new Review(revieweeId, UUID.randomUUID(), transactionId, UUID.randomUUID(), 4, "Good");

    when(reviewRepository.findByTransactionId(transactionId)).thenReturn(Optional.of(existing));

    assertThatThrownBy(() -> reviewService.createReview(UUID.randomUUID(), request))
        .isInstanceOf(DuplicateReviewException.class);

    verify(reviewRepository, never()).save(any());
  }

  @Test
  void should_stillSaveReview_even_when_revieweeHasNoProfile() {
    UUID reviewerId = UUID.randomUUID();
    UUID revieweeId = UUID.randomUUID();
    UUID transactionId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    CreateReviewRequest request =
        new CreateReviewRequest(transactionId, revieweeId, listingId, 3, "Decent");

    Review savedReview = new Review(revieweeId, reviewerId, transactionId, listingId, 3, "Decent");
    ReflectionTestUtils.setField(savedReview, "id", UUID.randomUUID());

    when(reviewRepository.findByTransactionId(transactionId)).thenReturn(Optional.empty());
    when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
    when(reviewRepository.computeAverageRating(revieweeId)).thenReturn(3.0);
    when(userProfileRepository.findByUserId(revieweeId)).thenReturn(Optional.empty());

    ReviewResponse response = reviewService.createReview(reviewerId, request);

    assertThat(response).isNotNull();
    verify(userProfileRepository, never()).save(any());
  }

  // ── trust score calculation ────────────────────────────────────────────────

  @Test
  void should_computeTrustScore_rounded_to_two_decimal_places() {
    UUID reviewerId = UUID.randomUUID();
    UUID revieweeId = UUID.randomUUID();
    UUID transactionId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    CreateReviewRequest request =
        new CreateReviewRequest(transactionId, revieweeId, listingId, 4, "Good");

    Review savedReview = new Review(revieweeId, reviewerId, transactionId, listingId, 4, "Good");
    ReflectionTestUtils.setField(savedReview, "id", UUID.randomUUID());

    UserProfile profile = new UserProfile(revieweeId);

    // Average that would be 4.666... → rounds to 4.67
    when(reviewRepository.findByTransactionId(transactionId)).thenReturn(Optional.empty());
    when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
    when(reviewRepository.computeAverageRating(revieweeId)).thenReturn(4.6666666);
    when(userProfileRepository.findByUserId(revieweeId)).thenReturn(Optional.of(profile));
    when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(inv -> inv.getArgument(0));

    reviewService.createReview(reviewerId, request);

    // Capture what trust score was set on the profile
    verify(userProfileRepository).save(any(UserProfile.class));
    assertThat(profile.getTrustScore()).isEqualByComparingTo(new BigDecimal("4.67"));
  }

  // ── getReviewsForUser ──────────────────────────────────────────────────────

  @Test
  void should_returnPagedReviews_for_reviewee() {
    UUID revieweeId = UUID.randomUUID();
    PageRequest pageable = PageRequest.of(0, 10);

    Review r1 =
        new Review(
            revieweeId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 5, "Excellent!");
    ReflectionTestUtils.setField(r1, "id", UUID.randomUUID());
    Review r2 =
        new Review(
            revieweeId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 4, "Very good");
    ReflectionTestUtils.setField(r2, "id", UUID.randomUUID());

    Page<Review> reviewPage = new PageImpl<>(List.of(r1, r2), pageable, 2);
    when(reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(revieweeId, pageable))
        .thenReturn(reviewPage);

    Page<ReviewResponse> result = reviewService.getReviewsForUser(revieweeId, pageable);

    assertThat(result.getContent()).hasSize(2);
    assertThat(result.getTotalElements()).isEqualTo(2);
    assertThat(result.getContent()).allMatch(r -> r.revieweeId().equals(revieweeId));
  }

  @Test
  void should_returnEmptyPage_when_userHasNoReviews() {
    UUID revieweeId = UUID.randomUUID();
    PageRequest pageable = PageRequest.of(0, 10);

    when(reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(revieweeId, pageable))
        .thenReturn(Page.empty(pageable));

    Page<ReviewResponse> result = reviewService.getReviewsForUser(revieweeId, pageable);

    assertThat(result.getContent()).isEmpty();
  }
}
