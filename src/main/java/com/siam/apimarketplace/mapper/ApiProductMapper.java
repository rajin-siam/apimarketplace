package com.siam.apimarketplace.mapper;

import com.siam.apimarketplace.dto.*;
import com.siam.apimarketplace.entity.ApiProduct;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ApiItemMapper.class})
public interface ApiProductMapper {

    ApiProductDto toDto(ApiProduct apiProduct);


    @Mapping(target = "id", ignore = true)
    ApiProduct toEntity(ApiProductCreateDto dto);
}