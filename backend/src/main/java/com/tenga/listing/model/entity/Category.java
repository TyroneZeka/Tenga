package com.tenga.listing.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lst_categories")
public class Category extends BaseEntity {

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(length = 100)
  private String slug;

  @Column(length = 255)
  private String iconUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Category> children = new ArrayList<>();

  @Column(nullable = false)
  private int sortOrder;

  @Column(nullable = false)
  private boolean active;

  protected Category() {}

  public Category(String name, String slug, Category parent) {
    this.name = name;
    this.slug = slug;
    this.parent = parent;
    this.active = true;
    this.sortOrder = 0;
  }

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public Category getParent() {
    return parent;
  }

  public List<Category> getChildren() {
    return children;
  }

  public int getSortOrder() {
    return sortOrder;
  }

  public boolean isActive() {
    return active;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
  }
}
