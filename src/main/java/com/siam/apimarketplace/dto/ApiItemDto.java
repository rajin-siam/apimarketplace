package com.siam.apimarketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ApiItemDto(
        @NotNull(message = "ID is required")
        @Positive(message = "ID must be positive")
        Integer id,

        @NotBlank(message = "Tier name is required")
        String tierName,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price,

        @NotNull(message = "API product ID is required")
        @Positive(message = "API product ID must be positive")
        Integer apiProductId
) {}