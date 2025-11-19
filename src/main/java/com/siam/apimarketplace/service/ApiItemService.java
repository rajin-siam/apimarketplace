package com.siam.apimarketplace.service;

import com.siam.apimarketplace.dto.ApiItemCreateDto;
import com.siam.apimarketplace.dto.ApiItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiItemService {

    ApiItemDto createItem(ApiItemCreateDto createDto);

    List<ApiItemDto> getAllItems();

    ApiItemDto getItemById(Integer id);

    List<ApiItemDto> getItemsByProductId(Integer productId);

    ApiItemDto updateItem(Integer id, ApiItemDto updateDto);

    void deleteItem(Integer id);

    void deleteAllItemsByProductId(Integer productId);
}
