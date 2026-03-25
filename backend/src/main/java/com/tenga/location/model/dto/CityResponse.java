package com.tenga.location.model.dto;

import java.util.UUID;

public record CityResponse(
    UUID id, String name, String province, double latitude, double longitude) {}
