package com.siam.apimarketplace.dto;

public record ApiItemDto(
        Integer id,
        String tierName,
        Double price,
        Integer apiProductId
) {}