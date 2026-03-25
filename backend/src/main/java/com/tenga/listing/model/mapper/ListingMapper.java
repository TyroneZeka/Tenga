package com.tenga.listing.model.mapper;

import com.tenga.listing.model.dto.ListingResponse;
import com.tenga.listing.model.entity.Listing;
import com.tenga.listing.model.entity.ListingImage;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ListingMapper {

  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "categoryName", source = "category.name")
  @Mapping(target = "imageUrls", source = "images", qualifiedByName = "toImageUrls")
  ListingResponse toResponse(Listing listing);

  @Named("toImageUrls")
  default List<String> toImageUrls(List<ListingImage> images) {
    if (images == null) return List.of();
    return images.stream().map(ListingImage::getUrl).toList();
  }
}
