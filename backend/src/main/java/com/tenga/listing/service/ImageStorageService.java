package com.tenga.listing.service;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

  /**
   * Validates, compresses, and uploads an image.
   *
   * @return the storage key (S3 object key) of the uploaded file
   */
  String upload(MultipartFile file, UUID listingId);

  /** Returns the public CDN URL for a given storage key. */
  String getUrl(String storageKey);

  /** Deletes the object from storage. */
  void delete(String storageKey);
}
