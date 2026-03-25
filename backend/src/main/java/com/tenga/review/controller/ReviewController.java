package com.tenga.review.controller;

import com.tenga.review.model.dto.CreateReviewRequest;
import com.tenga.review.model.dto.ReviewResponse;
import com.tenga.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Reviews", description = "Buyer reviews for sellers after completed transactions")
public class ReviewController {

  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping("/reviews")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Submit a review for a completed transaction")
  public ResponseEntity<ReviewResponse> createReview(
      @AuthenticationPrincipal UUID userId, @Valid @RequestBody CreateReviewRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(reviewService.createReview(userId, request));
  }

  @GetMapping("/users/{userId}/reviews")
  @Operation(summary = "Get reviews for a user (public)")
  public ResponseEntity<Page<ReviewResponse>> getReviews(
      @PathVariable UUID userId, @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(reviewService.getReviewsForUser(userId, pageable));
  }
}
