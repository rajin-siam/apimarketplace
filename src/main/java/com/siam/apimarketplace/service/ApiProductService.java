package com.siam.apimarketplace.service;

import com.siam.apimarketplace.dto.ApiProductCreateDto;
import com.siam.apimarketplace.dto.ApiProductDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ApiProductService {

    ApiProductDto createProduct(ApiProductCreateDto createDto);

    List<ApiProductDto> getAllProducts();

    ApiProductDto getProductById(Integer id);

    ApiProductDto updateProduct(Integer id, ApiProductDto updateDto);

    void deleteProduct(Integer id);
}
