package com.tenga.user.controller;

import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.user.model.dto.UpdateProfileRequest;
import com.tenga.user.model.dto.UserProfileResponse;
import com.tenga.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User profiles")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a user's public profile")
  public ResponseEntity<UserProfileResponse> getProfile(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getProfile(id));
  }

  @PatchMapping("/me")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Update the authenticated user's profile")
  public ResponseEntity<UserProfileResponse> updateProfile(
      @AuthenticationPrincipal UUID userId, @Valid @RequestBody UpdateProfileRequest request) {
    return ResponseEntity.ok(userService.updateProfile(userId, request));
  }

  @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Upload or replace the authenticated user's avatar")
  public ResponseEntity<UserProfileResponse> uploadAvatar(
      @AuthenticationPrincipal UUID userId, @RequestParam("file") MultipartFile file) {
    return ResponseEntity.ok(userService.uploadAvatar(userId, file));
  }

  @GetMapping("/{id}/listings")
  @Operation(summary = "Get all listings posted by a user")
  public ResponseEntity<Page<ListingResponse>> getUserListings(
      @PathVariable UUID id, @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(userService.getUserListings(id, pageable));
  }
}
