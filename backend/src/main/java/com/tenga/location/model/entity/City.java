package com.tenga.location.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "loc_cities")
public class City extends BaseEntity {

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 100)
  private String province;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;

  @Column(nullable = false)
  private boolean active;

  protected City() {}

  public String getName() {
    return name;
  }

  public String getProvince() {
    return province;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public boolean isActive() {
    return active;
  }
}
