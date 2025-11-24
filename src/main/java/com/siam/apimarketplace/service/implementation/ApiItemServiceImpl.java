package com.siam.apimarketplace.service.implementation;

import com.siam.apimarketplace.dto.ApiItemCreateDto;
import com.siam.apimarketplace.dto.ApiItemDto;
import com.siam.apimarketplace.entity.ApiItem;
import com.siam.apimarketplace.entity.ApiProduct;
import com.siam.apimarketplace.mapper.ApiItemMapper;
import com.siam.apimarketplace.repository.ApiItemRepository;
import com.siam.apimarketplace.repository.ApiProductRepository;
import com.siam.apimarketplace.service.ApiItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiItemServiceImpl implements ApiItemService {

    private final ApiItemRepository itemRepository;
    private final ApiProductRepository productRepository;
    private final ApiItemMapper itemMapper;

    @Transactional
    public ApiItemDto createItem(ApiItemCreateDto createDto) {
        ApiProduct product = productRepository.findById(createDto.apiProductId())
                .orElseThrow(() -> new RuntimeException(
                        "Product not found with id: " + createDto.apiProductId()));
        ApiItem item = itemMapper.toEntity(createDto);

        item.setApiProduct(product);

        ApiItem savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem);
    }

    public List<ApiItemDto> getAllItems() {

        return itemRepository.findAll().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    public ApiItemDto getItemById(Long id) {

        ApiItem item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        return itemMapper.toDto(item);
    }

    public List<ApiItemDto> getItemsByProductId(Long productId) {
        return itemRepository.findByApiProductId(productId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApiItemDto updateItem(Long id, ApiItemDto updateDto) {
        ApiItem existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        existingItem.setTierName(updateDto.tierName());
        existingItem.setPrice(updateDto.price());

        if (updateDto.apiProductId() != null &&
                !updateDto.apiProductId().equals(existingItem.getApiProduct().getId())) {

            ApiProduct newProduct = productRepository.findById(updateDto.apiProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id: " + updateDto.apiProductId()));

            existingItem.setApiProduct(newProduct);
        }

        ApiItem updated = itemRepository.save(existingItem);
        return itemMapper.toDto(updated);
    }

    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }

        itemRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllItemsByProductId(Long productId) {
        List<ApiItem> items = itemRepository.findByApiProductId(productId);

        if (!items.isEmpty()) {
            itemRepository.deleteAll(items);
        }
    }
}