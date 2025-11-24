package com.siam.apimarketplace.service;

import com.siam.apimarketplace.dto.ApiItemCreateDto;
import com.siam.apimarketplace.dto.ApiItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiItemService {

    ApiItemDto createItem(ApiItemCreateDto createDto);

    List<ApiItemDto> getAllItems();

    ApiItemDto getItemById(Long id);

    List<ApiItemDto> getItemsByProductId(Long productId);

    ApiItemDto updateItem(Long id, ApiItemDto updateDto);

    void deleteItem(Long id);

    void deleteAllItemsByProductId(Long productId);
}
