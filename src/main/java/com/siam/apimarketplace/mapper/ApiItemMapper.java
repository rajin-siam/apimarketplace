package com.siam.apimarketplace.mapper;

import com.siam.apimarketplace.dto.*;
import com.siam.apimarketplace.entity.ApiItem;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ApiItemMapper {

    default ApiItemDto toDto(ApiItem apiItem) {
        if (apiItem == null) {
            return null;
        }

        Integer productId = null;
        if (apiItem.getApiProduct() != null) {
            productId = apiItem.getApiProduct().getId();
        }

        return new ApiItemDto(
                apiItem.getId(),
                apiItem.getTierName(),
                apiItem.getPrice(),
                productId
        );
    }

    @Mapping(target = "apiProduct", ignore = true)
    ApiItem toEntity(ApiItemDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "apiProduct", ignore = true)
    ApiItem toEntity(ApiItemCreateDto dto);
}