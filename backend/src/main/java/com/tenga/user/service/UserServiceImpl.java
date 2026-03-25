package com.tenga.user.service;

import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.listing.model.enums.ListingStatus;
import com.tenga.listing.model.mapper.ListingMapper;
import com.tenga.listing.repository.ListingRepository;
import com.tenga.user.model.dto.UpdateProfileRequest;
import com.tenga.user.model.dto.UserProfileResponse;
import com.tenga.user.model.entity.UserProfile;
import com.tenga.user.repository.UserProfileRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserProfileRepository profileRepository;
  private final AvatarStorageService avatarStorageService;
  private final ListingRepository listingRepository;
  private final ListingMapper listingMapper;

  public UserServiceImpl(
      UserProfileRepository profileRepository,
      AvatarStorageService avatarStorageService,
      ListingRepository listingRepository,
      ListingMapper listingMapper) {
    this.profileRepository = profileRepository;
    this.avatarStorageService = avatarStorageService;
    this.listingRepository = listingRepository;
    this.listingMapper = listingMapper;
  }

  @Override
  @Transactional
  public UserProfileResponse getProfile(UUID userId) {
    UserProfile profile = getOrCreate(userId);
    return toResponse(profile, userId);
  }

  @Override
  @Transactional
  public UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
    UserProfile profile = getOrCreate(userId);
    profile.updateDetails(request.displayName(), request.bio(), request.city(), request.suburb());
    log.info("Profile updated: userId={}", userId);
    return toResponse(profile, userId);
  }

  @Override
  @Transactional
  public UserProfileResponse uploadAvatar(UUID userId, MultipartFile file) {
    UserProfile profile = getOrCreate(userId);

    // Delete previous avatar if one exists
    avatarStorageService.delete(profile.getAvatarStorageKey());

    String storageKey = avatarStorageService.upload(file, userId);
    String url = avatarStorageService.getUrl(storageKey);
    profile.updateAvatar(storageKey, url);

    log.info("Avatar updated: userId={}", userId);
    return toResponse(profile, userId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ListingResponse> getUserListings(UUID userId, Pageable pageable) {
    return listingRepository
        .findBySellerIdAndDeletedAtIsNull(userId, pageable)
        .map(listingMapper::toResponse);
  }

  // --- private helpers ---

  private UserProfile getOrCreate(UUID userId) {
    return profileRepository
        .findByUserId(userId)
        .orElseGet(
            () -> {
              UserProfile newProfile = new UserProfile(userId);
              profileRepository.save(newProfile);
              log.info("Profile auto-created: userId={}", userId);
              return newProfile;
            });
  }

  private UserProfileResponse toResponse(UserProfile profile, UUID userId) {
    long activeListingCount =
        listingRepository.countBySellerIdAndStatusAndDeletedAtIsNull(userId, ListingStatus.ACTIVE);
    return new UserProfileResponse(
        profile.getId(),
        profile.getUserId(),
        profile.getDisplayName(),
        profile.getBio(),
        profile.getAvatarUrl(),
        profile.getCity(),
        profile.getSuburb(),
        activeListingCount,
        profile.getTrustScore(),
        profile.getCreatedAt());
  }
}
