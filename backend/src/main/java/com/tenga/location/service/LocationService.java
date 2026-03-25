package com.tenga.location.service;

import com.tenga.location.model.dto.CityResponse;
import com.tenga.location.model.dto.SafeMeetupPointResponse;
import java.util.List;
import java.util.UUID;

public interface LocationService {

  /** Returns all active cities, or filters by name if a query string is provided. */
  List<CityResponse> getCities(String query);

  /** Returns active safe meetup points for the given city. */
  List<SafeMeetupPointResponse> getMeetupPoints(UUID cityId);
}
