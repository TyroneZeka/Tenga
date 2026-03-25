package com.tenga.listing.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lst_listing_images")
public class ListingImage extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "listing_id", nullable = false)
  private Listing listing;

  @Column(nullable = false, length = 512)
  private String storageKey;

  @Column(nullable = false, length = 512)
  private String url;

  @Column(nullable = false)
  private int sortOrder;

  protected ListingImage() {}

  public ListingImage(Listing listing, String storageKey, String url, int sortOrder) {
    this.listing = listing;
    this.storageKey = storageKey;
    this.url = url;
    this.sortOrder = sortOrder;
  }

  public Listing getListing() {
    return listing;
  }

  public String getStorageKey() {
    return storageKey;
  }

  public String getUrl() {
    return url;
  }

  public int getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
  }
}
