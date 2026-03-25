package com.tenga.review.model.mapper;

import com.tenga.review.model.dto.ReviewResponse;
import com.tenga.review.model.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

  ReviewResponse toResponse(Review review);
}
