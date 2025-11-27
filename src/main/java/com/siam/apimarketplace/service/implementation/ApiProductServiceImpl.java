package com.siam.apimarketplace.service.implementation;

import com.siam.apimarketplace.dto.*;
import com.siam.apimarketplace.entity.ApiItem;
import com.siam.apimarketplace.entity.ApiProduct;
import com.siam.apimarketplace.mapper.ApiItemMapper;
import com.siam.apimarketplace.mapper.ApiProductMapper;
import com.siam.apimarketplace.repository.ApiItemRepository;
import com.siam.apimarketplace.repository.ApiProductRepository;
import com.siam.apimarketplace.service.ApiProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j  // Lombok annotation for logging
public class ApiProductServiceImpl implements ApiProductService {

    private final ApiProductRepository productRepository;
    private final ApiItemRepository itemRepository;
    private final ApiProductMapper productMapper;
    private final ApiItemMapper itemMapper;

    @Transactional
    @CacheEvict(value = "allProducts", allEntries = true)
    public ApiResponse<Void> createProduct(ApiProductCreateDto createDto) {
        log.info("Creating new product: {}", createDto.name());

        ApiProduct product = productMapper.toEntity(createDto);
        product.setIsActive(true);

        ApiProduct savedProduct = productRepository.save(product);

        log.info("Product created with ID: {}", savedProduct.getId());

        // Save associated items if present
        List<ApiItemCreateDto> itemDtos = createDto.apiItems();
        if (itemDtos != null && !itemDtos.isEmpty()) {
            List<ApiItem> items = itemDtos.stream()
                    .map(itemDto -> {
                        ApiItem item = itemMapper.toEntity(itemDto);
                        item.setApiProduct(savedProduct);
                        item.setIsActive(true);
                        return item;
                    })
                    .collect(Collectors.toList());
            itemRepository.saveAll(items);
            log.info("Created {} items for product ID: {}", items.size(), savedProduct.getId());
        }

        return new ApiResponse<>("Product Created Successfully", true, null);
    }


    @Cacheable(value = "allProducts")
    public List<ApiProductDto> getAllProducts() {
        log.info("Fetching all products from database");
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }


    @Cacheable(value = "products", key = "#id")
    public ApiResponse<ApiProductDto> getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        ApiProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return new ApiResponse<>("Product found", true, productMapper.toDto(product));
    }


    @Transactional
    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "allProducts", allEntries = true)
    public ApiResponse<ApiProductDto> updateProduct(Long id, ApiProductDto updateDto) {
        ApiProduct existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        existingProduct.setName(updateDto.name());
        existingProduct.setEndpoint(updateDto.endpoint());
        existingProduct.setFreeQuota(updateDto.freeQuota());
        ApiProduct updated = productRepository.save(existingProduct);
        return new ApiResponse<>("Product updated successfully", true, productMapper.toDto(updated));
    }


    @Transactional
    @CacheEvict(value = {"products", "allProducts"}, key = "#id")
    public ApiResponse<String> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        return new ApiResponse<>("Product deleted successfully", true, null);
    }
}