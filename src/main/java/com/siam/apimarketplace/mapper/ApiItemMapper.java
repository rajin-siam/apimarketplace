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

        Long productId = null;
        if (apiItem.getApiProduct() != null) {
            productId = apiItem.getApiProductId();
        }

        return new ApiItemDto(
                apiItem.getId(),
                apiItem.getTierName(),
                apiItem.getPrice(),
                productId
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "apiProduct", ignore = true)
    ApiItem toEntity(ApiItemCreateDto dto);
}