package com.tenga.listing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.common.exception.ResourceNotFoundException;
import com.tenga.listing.model.dto.CreateListingRequest;
import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.listing.model.entity.Category;
import com.tenga.listing.model.enums.Condition;
import com.tenga.listing.model.enums.Currency;
import com.tenga.listing.model.mapper.ListingMapperImpl;
import com.tenga.listing.repository.CategoryRepository;
import com.tenga.listing.repository.ListingRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ListingServiceImplTest {

  @Mock private ListingRepository listingRepository;
  @Mock private CategoryRepository categoryRepository;
  @Mock private ImageStorageService imageStorageService;
  @Spy private ListingMapperImpl listingMapper;

  @InjectMocks private ListingServiceImpl listingService;

  @Test
  void should_throwResourceNotFoundException_when_categoryDoesNotExist() {
    ReflectionTestUtils.setField(listingService, "maxImages", 10);

    UUID categoryId = UUID.randomUUID();
    CreateListingRequest request =
        new CreateListingRequest(
            "iPhone 14 Pro",
            "Barely used, excellent condition",
            new BigDecimal("650"),
            Currency.USD,
            Condition.LIKE_NEW,
            categoryId,
            true,
            "Harare",
            "Avenues",
            null,
            null);

    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> listingService.create(request, UUID.randomUUID()))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void should_createListing_when_requestIsValid() {
    ReflectionTestUtils.setField(listingService, "maxImages", 10);

    UUID categoryId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();

    Category category = new Category("Electronics", "electronics", null);
    ReflectionTestUtils.setField(category, "id", categoryId);

    CreateListingRequest request =
        new CreateListingRequest(
            "Samsung TV 55 inch",
            "Smart TV in great working condition, no dead pixels",
            new BigDecimal("350"),
            Currency.USD,
            Condition.GOOD,
            categoryId,
            true,
            "Bulawayo",
            "Suburbs",
            null,
            null);

    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(listingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    ListingResponse response = listingService.create(request, sellerId);

    assertThat(response.title()).isEqualTo("Samsung TV 55 inch");
    assertThat(response.currency()).isEqualTo(Currency.USD);
    assertThat(response.sellerId()).isEqualTo(sellerId);
    verify(listingRepository).save(any());
  }
}
