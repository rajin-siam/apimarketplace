package com.siam.apimarketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.Valid;
import java.util.List;

public record ApiProductDto(
        @NotNull(message = "ID is required")
        @Positive(message = "ID must be positive")
        Long id,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Endpoint is required")
        String endpoint,

        @NotNull(message = "Free quota is required")
        @PositiveOrZero(message = "Free quota must be zero or positive")
        Double freeQuota,

        @Valid
        List<ApiItemDto> apiItems
) {}