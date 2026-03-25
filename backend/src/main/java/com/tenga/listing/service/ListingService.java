package com.tenga.listing.service;

import com.tenga.listing.model.dto.CreateListingRequest;
import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.listing.model.dto.ListingSearchParams;
import com.tenga.listing.model.dto.UpdateListingRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ListingService {

  ListingResponse create(CreateListingRequest request, UUID sellerId);

  ListingResponse getById(UUID id);

  ListingResponse update(UUID id, UpdateListingRequest request, UUID sellerId);

  void delete(UUID id, UUID sellerId);

  ListingResponse publish(UUID id, UUID sellerId);

  ListingResponse markSold(UUID id, UUID sellerId);

  Page<ListingResponse> search(ListingSearchParams params, Pageable pageable);

  Page<ListingResponse> getByseller(UUID sellerId, Pageable pageable);

  ListingResponse addImage(UUID listingId, MultipartFile file, UUID sellerId);

  void deleteImage(UUID listingId, UUID imageId, UUID sellerId);
}
