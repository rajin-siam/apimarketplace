package com.siam.apimarketplace.service;

import com.siam.apimarketplace.dto.ApiProductCreateDto;
import com.siam.apimarketplace.dto.ApiProductDto;
import com.siam.apimarketplace.dto.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ApiProductService {

    ApiResponse createProduct(ApiProductCreateDto createDto);

    List<ApiProductDto> getAllProducts();

    ApiResponse<ApiProductDto> getProductById(Long id);

    ApiResponse<ApiProductDto> updateProduct(Long id, ApiProductDto updateDto);

    ApiResponse<String> deleteProduct(Long id);
}
