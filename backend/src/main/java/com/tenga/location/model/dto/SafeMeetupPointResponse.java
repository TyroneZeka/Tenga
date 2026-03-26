package com.tenga.location.model.dto;

import java.util.UUID;

public record SafeMeetupPointResponse(
    UUID id,
    String name,
    String description,
    String address,
    double latitude,
    double longitude,
    String cityName) {}
