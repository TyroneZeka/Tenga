package com.tenga.listing.controller;

import com.tenga.listing.model.dto.CreateListingRequest;
import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.listing.model.dto.ListingSearchParams;
import com.tenga.listing.model.dto.UpdateListingRequest;
import com.tenga.listing.model.enums.Condition;
import com.tenga.listing.model.enums.Currency;
import com.tenga.listing.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/listings")
@Tag(name = "Listings", description = "Create, search, and manage marketplace listings")
public class ListingController {

  private final ListingService listingService;

  public ListingController(ListingService listingService) {
    this.listingService = listingService;
  }

  @GetMapping
  @Operation(summary = "Search listings")
  public ResponseEntity<Page<ListingResponse>> search(
      @RequestParam(required = false) String query,
      @RequestParam(required = false) UUID categoryId,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) Currency currency,
      @RequestParam(required = false) Condition condition,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) Integer radiusKm,
      @PageableDefault(size = 20) Pageable pageable) {
    ListingSearchParams params =
        new ListingSearchParams(
            query,
            categoryId,
            minPrice,
            maxPrice,
            currency,
            condition,
            city,
            latitude,
            longitude,
            radiusKm);
    return ResponseEntity.ok(listingService.search(params, pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a listing by ID")
  public ResponseEntity<ListingResponse> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(listingService.getById(id));
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Create a new listing (saved as draft)")
  public ResponseEntity<ListingResponse> create(
      @Valid @RequestBody CreateListingRequest request, @AuthenticationPrincipal UUID sellerId) {
    return ResponseEntity.status(HttpStatus.CREATED).body(listingService.create(request, sellerId));
  }

  @PutMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Update a listing")
  public ResponseEntity<ListingResponse> update(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateListingRequest request,
      @AuthenticationPrincipal UUID sellerId) {
    return ResponseEntity.ok(listingService.update(id, request, sellerId));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Delete (soft-delete) a listing")
  public ResponseEntity<Void> delete(
      @PathVariable UUID id, @AuthenticationPrincipal UUID sellerId) {
    listingService.delete(id, sellerId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/publish")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Publish a draft listing")
  public ResponseEntity<ListingResponse> publish(
      @PathVariable UUID id, @AuthenticationPrincipal UUID sellerId) {
    return ResponseEntity.ok(listingService.publish(id, sellerId));
  }

  @PostMapping("/{id}/sold")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Mark a listing as sold")
  public ResponseEntity<ListingResponse> markSold(
      @PathVariable UUID id, @AuthenticationPrincipal UUID sellerId) {
    return ResponseEntity.ok(listingService.markSold(id, sellerId));
  }

  @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Upload an image for a listing")
  public ResponseEntity<ListingResponse> addImage(
      @PathVariable UUID id,
      @RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal UUID sellerId) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(listingService.addImage(id, file, sellerId));
  }

  @DeleteMapping("/{id}/images/{imageId}")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Delete an image from a listing")
  public ResponseEntity<Void> deleteImage(
      @PathVariable UUID id, @PathVariable UUID imageId, @AuthenticationPrincipal UUID sellerId) {
    listingService.deleteImage(id, imageId, sellerId);
    return ResponseEntity.noContent().build();
  }
}
