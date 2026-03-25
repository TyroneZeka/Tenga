package com.tenga.user.service;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarStorageService {

  /**
   * Validates and uploads an avatar image, replacing any previous avatar for this user.
   *
   * @return the storage key of the uploaded file
   */
  String upload(MultipartFile file, UUID userId);

  /** Returns the public URL for a given storage key. */
  String getUrl(String storageKey);

  /** Deletes the avatar object from storage. No-op if storageKey is null. */
  void delete(String storageKey);
}
