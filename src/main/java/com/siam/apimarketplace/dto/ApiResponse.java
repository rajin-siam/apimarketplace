package com.siam.apimarketplace.dto;

public record ApiResponse<T>(
    String message,
    Boolean success,
    T data
) {}
