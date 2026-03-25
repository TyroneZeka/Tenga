package com.tenga.listing.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import com.tenga.listing.model.enums.Condition;
import com.tenga.listing.model.enums.Currency;
import com.tenga.listing.model.enums.ListingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lst_listings")
public class Listing extends BaseEntity {

  @Column(nullable = false, length = 150)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Currency currency;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Condition condition;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ListingStatus status;

  @Column(nullable = false)
  private UUID sellerId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(length = 100)
  private String city;

  @Column(length = 100)
  private String suburb;

  /** Latitude for proximity search (stored separately for non-PostGIS queries). */
  private Double latitude;

  /** Longitude for proximity search. */
  private Double longitude;

  @Column(nullable = false)
  private int viewCount;

  @Column(nullable = false)
  private boolean negotiable;

  private Instant expiresAt;

  private Instant deletedAt;

  @OneToMany(
      mappedBy = "listing",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @OrderBy("sortOrder ASC")
  private List<ListingImage> images = new ArrayList<>();

  protected Listing() {}

  public Listing(
      String title,
      String description,
      BigDecimal price,
      Currency currency,
      Condition condition,
      UUID sellerId,
      Category category) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.currency = currency;
    this.condition = condition;
    this.sellerId = sellerId;
    this.category = category;
    this.status = ListingStatus.DRAFT;
    this.viewCount = 0;
    this.negotiable = false;
  }

  // --- getters ---

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Currency getCurrency() {
    return currency;
  }

  public Condition getCondition() {
    return condition;
  }

  public ListingStatus getStatus() {
    return status;
  }

  public UUID getSellerId() {
    return sellerId;
  }

  public Category getCategory() {
    return category;
  }

  public String getCity() {
    return city;
  }

  public String getSuburb() {
    return suburb;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public int getViewCount() {
    return viewCount;
  }

  public boolean isNegotiable() {
    return negotiable;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public List<ListingImage> getImages() {
    return images;
  }

  // --- state transitions ---

  public void publish() {
    this.status = ListingStatus.ACTIVE;
    this.expiresAt = Instant.now().plusSeconds(60L * 60 * 24 * 90); // 90 days
  }

  public void markSold() {
    this.status = ListingStatus.SOLD;
  }

  public void markReserved() {
    this.status = ListingStatus.RESERVED;
  }

  public void softDelete() {
    this.deletedAt = Instant.now();
    this.status = ListingStatus.REMOVED;
  }

  public void incrementViewCount() {
    this.viewCount++;
  }

  // --- mutations ---

  public void update(
      String title,
      String description,
      BigDecimal price,
      Currency currency,
      Condition condition,
      boolean negotiable,
      String city,
      String suburb) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.currency = currency;
    this.condition = condition;
    this.negotiable = negotiable;
    this.city = city;
    this.suburb = suburb;
  }

  public void setLocation(String city, String suburb, Double latitude, Double longitude) {
    this.city = city;
    this.suburb = suburb;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
