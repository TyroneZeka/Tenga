package com.tenga.location.repository;

import com.tenga.location.model.entity.SafeMeetupPoint;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafeMeetupPointRepository extends JpaRepository<SafeMeetupPoint, UUID> {

  List<SafeMeetupPoint> findByCityIdAndActiveTrueOrderByNameAsc(UUID cityId);
}
