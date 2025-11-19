package com.siam.apimarketplace.dto;
import java.util.List;


public record ApiProductCreateDto(
        String name,
        String endpoint,
        Double freeQuota,
        List<ApiItemCreateDto> apiItems
) {}