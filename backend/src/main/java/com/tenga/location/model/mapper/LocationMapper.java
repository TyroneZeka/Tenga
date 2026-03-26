package com.tenga.location.model.mapper;

import com.tenga.location.model.dto.CityResponse;
import com.tenga.location.model.dto.SafeMeetupPointResponse;
import com.tenga.location.model.entity.City;
import com.tenga.location.model.entity.SafeMeetupPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

  CityResponse toResponse(City city);

  @Mapping(target = "cityName", source = "city.name")
  SafeMeetupPointResponse toResponse(SafeMeetupPoint point);
}
