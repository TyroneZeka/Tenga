package com.tenga.location.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.location.model.dto.CityResponse;
import com.tenga.location.model.dto.SafeMeetupPointResponse;
import com.tenga.location.model.entity.City;
import com.tenga.location.model.entity.SafeMeetupPoint;
import com.tenga.location.model.mapper.LocationMapper;
import com.tenga.location.repository.CityRepository;
import com.tenga.location.repository.SafeMeetupPointRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

  @Mock private CityRepository cityRepository;
  @Mock private SafeMeetupPointRepository meetupPointRepository;
  @Mock private LocationMapper locationMapper;

  @InjectMocks private LocationServiceImpl locationService;

  @Test
  void should_returnAllActiveCities_when_noQueryProvided() {
    City harare = mock(City.class);
    City bulawayo = mock(City.class);
    CityResponse harareDto =
        new CityResponse(UUID.randomUUID(), "Harare", "Harare Metropolitan", -17.82, 31.05);
    CityResponse bulawayoDto =
        new CityResponse(UUID.randomUUID(), "Bulawayo", "Bulawayo Metropolitan", -20.15, 28.58);

    when(cityRepository.findByActiveTrueOrderByNameAsc()).thenReturn(List.of(harare, bulawayo));
    when(locationMapper.toResponse(harare)).thenReturn(harareDto);
    when(locationMapper.toResponse(bulawayo)).thenReturn(bulawayoDto);

    List<CityResponse> result = locationService.getCities(null);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).name()).isEqualTo("Harare");
    verify(cityRepository).findByActiveTrueOrderByNameAsc();
  }

  @Test
  void should_searchCities_when_queryIsProvided() {
    City harare = mock(City.class);
    CityResponse dto =
        new CityResponse(UUID.randomUUID(), "Harare", "Harare Metropolitan", -17.82, 31.05);

    when(cityRepository.searchByName("hara")).thenReturn(List.of(harare));
    when(locationMapper.toResponse(harare)).thenReturn(dto);

    List<CityResponse> result = locationService.getCities("hara");

    assertThat(result).hasSize(1);
    assertThat(result.get(0).name()).isEqualTo("Harare");
    verify(cityRepository).searchByName("hara");
  }

  @Test
  void should_trimQuery_when_queryHasLeadingOrTrailingWhitespace() {
    City mutare = mock(City.class);
    CityResponse dto = new CityResponse(UUID.randomUUID(), "Mutare", "Manicaland", -18.97, 32.67);

    when(cityRepository.searchByName("Mutare")).thenReturn(List.of(mutare));
    when(locationMapper.toResponse(mutare)).thenReturn(dto);

    List<CityResponse> result = locationService.getCities("  Mutare  ");

    assertThat(result).hasSize(1);
    // Trimmed "Mutare" was passed to the repository
    verify(cityRepository).searchByName("Mutare");
  }

  @Test
  void should_returnEmptyList_when_noCitiesMatch() {
    when(cityRepository.searchByName("xyz")).thenReturn(List.of());

    List<CityResponse> result = locationService.getCities("xyz");

    assertThat(result).isEmpty();
  }

  @Test
  void should_returnAllCities_when_queryIsBlank() {
    City city = mock(City.class);
    CityResponse dto = new CityResponse(UUID.randomUUID(), "Gweru", "Midlands", -19.45, 29.82);

    when(cityRepository.findByActiveTrueOrderByNameAsc()).thenReturn(List.of(city));
    when(locationMapper.toResponse(city)).thenReturn(dto);

    // Blank query should fall through to the "list all" branch
    List<CityResponse> result = locationService.getCities("   ");

    assertThat(result).hasSize(1);
    verify(cityRepository).findByActiveTrueOrderByNameAsc();
  }

  @Test
  void should_returnMeetupPoints_when_cityIdProvided() {
    UUID cityId = UUID.randomUUID();
    SafeMeetupPoint point = mock(SafeMeetupPoint.class);
    SafeMeetupPointResponse dto =
        new SafeMeetupPointResponse(
            UUID.randomUUID(),
            "Eastgate Mall",
            "Inside Eastgate",
            "Robert Mugabe Rd",
            -17.83,
            31.05,
            "Harare");

    when(meetupPointRepository.findByCityIdAndActiveTrueOrderByNameAsc(cityId))
        .thenReturn(List.of(point));
    when(locationMapper.toResponse(point)).thenReturn(dto);

    List<SafeMeetupPointResponse> result = locationService.getMeetupPoints(cityId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).name()).isEqualTo("Eastgate Mall");
    assertThat(result.get(0).cityName()).isEqualTo("Harare");
  }

  @Test
  void should_returnEmptyList_when_noMeetupPointsForCity() {
    UUID cityId = UUID.randomUUID();
    when(meetupPointRepository.findByCityIdAndActiveTrueOrderByNameAsc(cityId))
        .thenReturn(List.of());

    List<SafeMeetupPointResponse> result = locationService.getMeetupPoints(cityId);

    assertThat(result).isEmpty();
  }
}
