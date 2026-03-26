package com.tenga.user.service;

import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.user.model.dto.UpdateProfileRequest;
import com.tenga.user.model.dto.UserProfileResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  /** Returns the public profile for a user. Creates a default profile if none exists yet. */
  UserProfileResponse getProfile(UUID userId);

  /** Updates editable profile fields for the authenticated user. */
  UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);

  /** Uploads a new avatar, replacing any existing one. */
  UserProfileResponse uploadAvatar(UUID userId, MultipartFile file);

  /** Returns all non-deleted listings posted by a seller, newest first. */
  Page<ListingResponse> getUserListings(UUID userId, Pageable pageable);
}
