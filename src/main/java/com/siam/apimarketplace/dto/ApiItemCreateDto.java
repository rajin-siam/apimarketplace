package com.siam.apimarketplace.dto;

public record ApiItemCreateDto(
        String tierName,
        Double price,
        Integer apiProductId
) {}