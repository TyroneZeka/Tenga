package com.tenga.user.service;

import com.tenga.common.exception.BusinessException;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3AvatarStorageService implements AvatarStorageService {

  private static final Logger log = LoggerFactory.getLogger(S3AvatarStorageService.class);
  private static final Set<String> ALLOWED_CONTENT_TYPES =
      Set.of("image/jpeg", "image/png", "image/webp");
  private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB for avatars

  private final S3Client s3Client;
  private final String bucket;
  private final String endpoint;

  public S3AvatarStorageService(
      S3Client s3Client,
      @Value("${tenga.s3.bucket}") String bucket,
      @Value("${tenga.s3.endpoint}") String endpoint) {
    this.s3Client = s3Client;
    this.bucket = bucket;
    this.endpoint = endpoint;
  }

  @Override
  public String upload(MultipartFile file, UUID userId) {
    validateFile(file);

    String extension = getExtension(file.getContentType());
    // Fixed key per user — re-uploading overwrites the previous avatar
    String storageKey = "avatars/%s/avatar.%s".formatted(userId, extension);

    try {
      s3Client.putObject(
          PutObjectRequest.builder()
              .bucket(bucket)
              .key(storageKey)
              .contentType(file.getContentType())
              .build(),
          RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      log.info("Avatar uploaded: userId={}, key={}", userId, storageKey);
      return storageKey;
    } catch (IOException ex) {
      throw new BusinessException("Failed to read uploaded file");
    }
  }

  @Override
  public String getUrl(String storageKey) {
    return "%s/%s/%s".formatted(endpoint, bucket, storageKey);
  }

  @Override
  public void delete(String storageKey) {
    if (storageKey == null) return;
    s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(storageKey).build());
    log.info("Avatar deleted: key={}", storageKey);
  }

  private void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new BusinessException("File is empty");
    }
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new BusinessException("Avatar size exceeds maximum of 2MB");
    }
    if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
      throw new BusinessException("Only JPEG, PNG, and WebP images are allowed");
    }
  }

  private String getExtension(String contentType) {
    return switch (contentType) {
      case "image/jpeg" -> "jpg";
      case "image/png" -> "png";
      case "image/webp" -> "webp";
      default -> "jpg";
    };
  }
}
