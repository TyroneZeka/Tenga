package com.tenga.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.listing.model.enums.ListingStatus;
import com.tenga.listing.model.mapper.ListingMapper;
import com.tenga.listing.repository.ListingRepository;
import com.tenga.user.model.dto.UpdateProfileRequest;
import com.tenga.user.model.dto.UserProfileResponse;
import com.tenga.user.model.entity.UserProfile;
import com.tenga.user.repository.UserProfileRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserProfileRepository profileRepository;
  @Mock private AvatarStorageService avatarStorageService;
  @Mock private ListingRepository listingRepository;
  @Mock private ListingMapper listingMapper;

  @InjectMocks private UserServiceImpl userService;

  @Test
  void should_createProfile_when_noneExistsForUser() {
    UUID userId = UUID.randomUUID();
    when(profileRepository.findByUserId(userId)).thenReturn(Optional.empty());
    when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(listingRepository.countBySellerIdAndStatusAndDeletedAtIsNull(userId, ListingStatus.ACTIVE))
        .thenReturn(0L);

    UserProfileResponse response = userService.getProfile(userId);

    assertThat(response.userId()).isEqualTo(userId);
    verify(profileRepository).save(any(UserProfile.class));
  }

  @Test
  void should_returnExistingProfile_when_profileExists() {
    UUID userId = UUID.randomUUID();
    UserProfile existing = new UserProfile(userId);
    ReflectionTestUtils.setField(existing, "displayName", "Alice");

    when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
    when(listingRepository.countBySellerIdAndStatusAndDeletedAtIsNull(userId, ListingStatus.ACTIVE))
        .thenReturn(3L);

    UserProfileResponse response = userService.getProfile(userId);

    assertThat(response.displayName()).isEqualTo("Alice");
    assertThat(response.activeListingCount()).isEqualTo(3L);
    verify(profileRepository, never()).save(any());
  }

  @Test
  void should_updateProfileFields_when_updateRequested() {
    UUID userId = UUID.randomUUID();
    UserProfile profile = new UserProfile(userId);

    when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
    when(listingRepository.countBySellerIdAndStatusAndDeletedAtIsNull(eq(userId), any()))
        .thenReturn(0L);

    UpdateProfileRequest request = new UpdateProfileRequest("Bob", "Seller of fine goods", "Harare", "Avenues");
    UserProfileResponse response = userService.updateProfile(userId, request);

    assertThat(response.displayName()).isEqualTo("Bob");
    assertThat(response.bio()).isEqualTo("Seller of fine goods");
    assertThat(response.city()).isEqualTo("Harare");
    assertThat(response.suburb()).isEqualTo("Avenues");
  }
}
