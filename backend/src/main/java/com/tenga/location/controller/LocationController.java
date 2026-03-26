package com.tenga.location.controller;

import com.tenga.location.model.dto.CityResponse;
import com.tenga.location.model.dto.SafeMeetupPointResponse;
import com.tenga.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Locations", description = "Zimbabwe cities and safe meetup points")
public class LocationController {

  private final LocationService locationService;

  public LocationController(LocationService locationService) {
    this.locationService = locationService;
  }

  @GetMapping("/cities")
  @Operation(summary = "List or search cities (use ?q= for autocomplete)")
  public ResponseEntity<List<CityResponse>> getCities(@RequestParam(required = false) String q) {
    return ResponseEntity.ok(locationService.getCities(q));
  }

  @GetMapping("/cities/{id}/meetup-points")
  @Operation(summary = "Get safe meetup points for a city")
  public ResponseEntity<List<SafeMeetupPointResponse>> getMeetupPoints(@PathVariable UUID id) {
    return ResponseEntity.ok(locationService.getMeetupPoints(id));
  }
}
