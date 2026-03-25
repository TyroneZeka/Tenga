package com.tenga.listing.repository;

import com.tenga.listing.model.entity.Listing;
import com.tenga.listing.model.enums.ListingStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ListingRepository
    extends JpaRepository<Listing, UUID>, JpaSpecificationExecutor<Listing> {

  @Query("SELECT l FROM Listing l WHERE l.id = :id AND l.deletedAt IS NULL")
  Optional<Listing> findActiveById(UUID id);

  Page<Listing> findBySellerIdAndDeletedAtIsNull(UUID sellerId, Pageable pageable);

  Page<Listing> findByStatusAndDeletedAtIsNull(ListingStatus status, Pageable pageable);

  @Query(
      """
            SELECT l FROM Listing l
            WHERE l.status = 'ACTIVE'
              AND l.deletedAt IS NULL
              AND l.category.id = :categoryId
            """)
  Page<Listing> findActiveByCategoryId(UUID categoryId, Pageable pageable);

  long countBySellerIdAndStatusAndDeletedAtIsNull(UUID sellerId, ListingStatus status);
}
