package com.tenga.location.service;

import com.tenga.location.model.dto.CityResponse;
import com.tenga.location.model.dto.SafeMeetupPointResponse;
import com.tenga.location.model.mapper.LocationMapper;
import com.tenga.location.repository.CityRepository;
import com.tenga.location.repository.SafeMeetupPointRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {

  private final CityRepository cityRepository;
  private final SafeMeetupPointRepository meetupPointRepository;
  private final LocationMapper locationMapper;

  public LocationServiceImpl(
      CityRepository cityRepository,
      SafeMeetupPointRepository meetupPointRepository,
      LocationMapper locationMapper) {
    this.cityRepository = cityRepository;
    this.meetupPointRepository = meetupPointRepository;
    this.locationMapper = locationMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CityResponse> getCities(String query) {
    if (query != null && !query.isBlank()) {
      return cityRepository.searchByName(query.trim()).stream()
          .map(locationMapper::toResponse)
          .toList();
    }
    return cityRepository.findByActiveTrueOrderByNameAsc().stream()
        .map(locationMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<SafeMeetupPointResponse> getMeetupPoints(UUID cityId) {
    return meetupPointRepository.findByCityIdAndActiveTrueOrderByNameAsc(cityId).stream()
        .map(locationMapper::toResponse)
        .toList();
  }
}
