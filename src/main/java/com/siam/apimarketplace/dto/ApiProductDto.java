package com.siam.apimarketplace.dto;


import java.util.List;

public record ApiProductDto(
        Integer id,
        String name,
        String endpoint,
        Double freeQuota,
        List<ApiItemDto> apiItems
) {}
