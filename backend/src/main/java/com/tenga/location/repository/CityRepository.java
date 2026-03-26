package com.tenga.location.repository;

import com.tenga.location.model.entity.City;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, UUID> {

  List<City> findByActiveTrueOrderByNameAsc();

  @Query(
      "SELECT c FROM City c WHERE c.active = TRUE AND lower(c.name) LIKE lower(concat('%', :query, '%')) ORDER BY c.name ASC")
  List<City> searchByName(String query);
}
