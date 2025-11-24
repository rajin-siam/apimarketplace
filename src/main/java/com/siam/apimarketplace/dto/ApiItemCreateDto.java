package com.siam.apimarketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ApiItemCreateDto(
        @NotBlank(message = "Tier name is required")
        String tierName,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price,
        Long apiProductId
) {}