package com.tenga.listing.service;

import com.tenga.listing.model.dto.ListingSearchParams;
import com.tenga.listing.model.entity.Listing;
import com.tenga.listing.model.enums.ListingStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

final class ListingSpecifications {

  private ListingSpecifications() {}

  static Specification<Listing> build(ListingSearchParams params) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Only show active, non-deleted listings in search
      predicates.add(cb.equal(root.get("status"), ListingStatus.ACTIVE));
      predicates.add(cb.isNull(root.get("deletedAt")));

      if (params.query() != null && !params.query().isBlank()) {
        String pattern = "%" + params.query().toLowerCase() + "%";
        predicates.add(
            cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)));
      }

      if (params.categoryId() != null) {
        predicates.add(cb.equal(root.get("category").get("id"), params.categoryId()));
      }

      if (params.minPrice() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("price"), params.minPrice()));
      }

      if (params.maxPrice() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("price"), params.maxPrice()));
      }

      if (params.currency() != null) {
        predicates.add(cb.equal(root.get("currency"), params.currency()));
      }

      if (params.condition() != null) {
        predicates.add(cb.equal(root.get("condition"), params.condition()));
      }

      if (params.city() != null && !params.city().isBlank()) {
        predicates.add(cb.equal(cb.lower(root.get("city")), params.city().toLowerCase()));
      }

      if (params.latitude() != null && params.longitude() != null) {
        int radiusKm = (params.radiusKm() != null) ? Math.min(params.radiusKm(), 100) : 25;
        double latDelta = radiusKm / 111.0;
        double lngDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(params.latitude())));
        predicates.add(cb.isNotNull(root.get("latitude")));
        predicates.add(
            cb.between(
                root.get("latitude"), params.latitude() - latDelta, params.latitude() + latDelta));
        predicates.add(
            cb.between(
                root.get("longitude"),
                params.longitude() - lngDelta,
                params.longitude() + lngDelta));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
