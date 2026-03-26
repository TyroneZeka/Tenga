package com.tenga.review.service;

import com.tenga.review.model.dto.CreateReviewRequest;
import com.tenga.review.model.dto.ReviewResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  ReviewResponse createReview(UUID reviewerId, CreateReviewRequest request);

  Page<ReviewResponse> getReviewsForUser(UUID revieweeId, Pageable pageable);
}
