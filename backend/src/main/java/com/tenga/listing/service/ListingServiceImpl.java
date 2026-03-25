package com.tenga.listing.service;

import com.tenga.common.exception.BusinessException;
import com.tenga.common.exception.ForbiddenException;
import com.tenga.common.exception.ResourceNotFoundException;
import com.tenga.listing.model.dto.CreateListingRequest;
import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.listing.model.dto.ListingSearchParams;
import com.tenga.listing.model.dto.UpdateListingRequest;
import com.tenga.listing.model.entity.Category;
import com.tenga.listing.model.entity.Listing;
import com.tenga.listing.model.entity.ListingImage;
import com.tenga.listing.model.enums.ListingStatus;
import com.tenga.listing.model.mapper.ListingMapper;
import com.tenga.listing.repository.CategoryRepository;
import com.tenga.listing.repository.ListingRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ListingServiceImpl implements ListingService {

  private static final Logger log = LoggerFactory.getLogger(ListingServiceImpl.class);

  private final ListingRepository listingRepository;
  private final CategoryRepository categoryRepository;
  private final ListingMapper listingMapper;
  private final ImageStorageService imageStorageService;

  @Value("${tenga.listing.max-images}")
  private int maxImages;

  public ListingServiceImpl(
      ListingRepository listingRepository,
      CategoryRepository categoryRepository,
      ListingMapper listingMapper,
      ImageStorageService imageStorageService) {
    this.listingRepository = listingRepository;
    this.categoryRepository = categoryRepository;
    this.listingMapper = listingMapper;
    this.imageStorageService = imageStorageService;
  }

  @Override
  @Transactional
  public ListingResponse create(CreateListingRequest request, UUID sellerId) {
    Category category =
        categoryRepository
            .findById(request.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

    Listing listing =
        new Listing(
            request.title(),
            request.description(),
            request.price(),
            request.currency(),
            request.condition(),
            sellerId,
            category);

    if (request.city() != null) {
      listing.setLocation(
          request.city(), request.suburb(), request.latitude(), request.longitude());
    }

    listingRepository.save(listing);
    log.info("Listing created: listingId={}, sellerId={}", listing.getId(), sellerId);
    return listingMapper.toResponse(listing);
  }

  @Override
  @Transactional(readOnly = true)
  public ListingResponse getById(UUID id) {
    Listing listing =
        listingRepository
            .findActiveById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Listing", id));

    // View count is incremented asynchronously via an event to avoid write contention
    return listingMapper.toResponse(listing);
  }

  @Override
  @Transactional
  public ListingResponse update(UUID id, UpdateListingRequest request, UUID sellerId) {
    Listing listing = requireOwnedListing(id, sellerId);

    if (listing.getStatus() == ListingStatus.SOLD || listing.getStatus() == ListingStatus.REMOVED) {
      throw new BusinessException("Cannot update a listing with status: " + listing.getStatus());
    }

    Category category =
        categoryRepository
            .findById(request.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

    listing.update(
        request.title(),
        request.description(),
        request.price(),
        request.currency(),
        request.condition(),
        request.negotiable(),
        request.city(),
        request.suburb());
    listing.setCategory(category);
    if (request.latitude() != null) {
      listing.setLocation(
          request.city(), request.suburb(), request.latitude(), request.longitude());
    }

    log.info("Listing updated: listingId={}, sellerId={}", id, sellerId);
    return listingMapper.toResponse(listing);
  }

  @Override
  @Transactional
  public void delete(UUID id, UUID sellerId) {
    Listing listing = requireOwnedListing(id, sellerId);
    listing.softDelete();
    log.info("Listing deleted: listingId={}, sellerId={}", id, sellerId);
  }

  @Override
  @Transactional
  public ListingResponse publish(UUID id, UUID sellerId) {
    Listing listing = requireOwnedListing(id, sellerId);
    if (listing.getImages().isEmpty()) {
      throw new BusinessException("A listing must have at least one image before publishing");
    }
    listing.publish();
    log.info("Listing published: listingId={}, sellerId={}", id, sellerId);
    return listingMapper.toResponse(listing);
  }

  @Override
  @Transactional
  public ListingResponse markSold(UUID id, UUID sellerId) {
    Listing listing = requireOwnedListing(id, sellerId);
    listing.markSold();
    log.info("Listing marked sold: listingId={}, sellerId={}", id, sellerId);
    return listingMapper.toResponse(listing);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ListingResponse> search(ListingSearchParams params, Pageable pageable) {
    Specification<Listing> spec = ListingSpecifications.build(params);
    return listingRepository.findAll(spec, pageable).map(listingMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ListingResponse> getByseller(UUID sellerId, Pageable pageable) {
    return listingRepository
        .findBySellerIdAndDeletedAtIsNull(sellerId, pageable)
        .map(listingMapper::toResponse);
  }

  @Override
  @Transactional
  public ListingResponse addImage(UUID listingId, MultipartFile file, UUID sellerId) {
    Listing listing = requireOwnedListing(listingId, sellerId);

    if (listing.getImages().size() >= maxImages) {
      throw new BusinessException("Maximum number of images (%d) reached".formatted(maxImages));
    }

    String storageKey = imageStorageService.upload(file, listingId);
    String url = imageStorageService.getUrl(storageKey);
    int sortOrder = listing.getImages().size();

    ListingImage image = new ListingImage(listing, storageKey, url, sortOrder);
    listing.getImages().add(image);

    log.info("Image added to listing: listingId={}, storageKey={}", listingId, storageKey);
    return listingMapper.toResponse(listing);
  }

  @Override
  @Transactional
  public void deleteImage(UUID listingId, UUID imageId, UUID sellerId) {
    Listing listing = requireOwnedListing(listingId, sellerId);

    ListingImage image =
        listing.getImages().stream()
            .filter(img -> img.getId().equals(imageId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Image", imageId));

    imageStorageService.delete(image.getStorageKey());
    listing.getImages().remove(image);

    // Re-order remaining images
    for (int i = 0; i < listing.getImages().size(); i++) {
      listing.getImages().get(i).setSortOrder(i);
    }

    log.info("Image deleted: listingId={}, imageId={}", listingId, imageId);
  }

  private Listing requireOwnedListing(UUID listingId, UUID sellerId) {
    Listing listing =
        listingRepository
            .findActiveById(listingId)
            .orElseThrow(() -> new ResourceNotFoundException("Listing", listingId));

    if (!listing.getSellerId().equals(sellerId)) {
      throw new ForbiddenException("You do not own this listing");
    }
    return listing;
  }
}
