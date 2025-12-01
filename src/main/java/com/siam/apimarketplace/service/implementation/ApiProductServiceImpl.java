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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiProductServiceImpl implements ApiProductService {

    private final ApiProductRepository productRepository;
    private final ApiItemRepository itemRepository;
    private final ApiProductMapper productMapper;
    private final ApiItemMapper itemMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_CACHE_KEY = "product:";
    private static final String ALL_PRODUCTS_CACHE_KEY = "allProducts";
    private static final long CACHE_TTL_MINUTES = 10;

    @Transactional
    public ApiResponse<Void> createProduct(ApiProductCreateDto createDto) {
        log.info("Creating new product: {}", createDto.name());

        ApiProduct product = productMapper.toEntity(createDto);
        product.setIsActive(true);

        ApiProduct savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());


        String cacheKey = PRODUCT_CACHE_KEY + savedProduct.getId();
        try {
            redisTemplate.opsForValue().set(
                    cacheKey,
                    savedProduct,
                    Duration.ofMinutes(CACHE_TTL_MINUTES)
            );
            log.info("Product cached in Redis with key: {}", cacheKey);
        } catch (Exception e) {
            log.warn("Failed to cache product in Redis: {}", e.getMessage());
        }

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

        try {
            redisTemplate.delete(ALL_PRODUCTS_CACHE_KEY);
            log.info("Invalidated all products cache");
        } catch (Exception e) {
            log.warn("Failed to invalidate all products cache: {}", e.getMessage());
        }

        return new ApiResponse<>("Product Created Successfully", true, null);
    }

    public List<ApiProductDto> getAllProducts() {
        log.info("Fetching all products");

        try {
            @SuppressWarnings("unchecked")
            List<ApiProduct> cachedProducts = (List<ApiProduct>) redisTemplate.opsForValue()
                    .get(ALL_PRODUCTS_CACHE_KEY);

            if (cachedProducts != null) {
                log.info("Cache HIT: Found {} products in Redis", cachedProducts.size());
                return cachedProducts.stream()
                        .map(productMapper::toDto)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("Redis cache read failed: {}", e.getMessage());
        }

        log.info("Cache MISS: Fetching from database");
        List<ApiProduct> products = productRepository.findAll();

        try {
            redisTemplate.opsForValue().set(
                    ALL_PRODUCTS_CACHE_KEY,
                    products,
                    Duration.ofMinutes(CACHE_TTL_MINUTES)
            );
            log.info("Cached {} products in Redis", products.size());
        } catch (Exception e) {
            log.warn("Failed to cache products: {}", e.getMessage());
        }

        // Step 4: Return DTOs to controller
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ApiResponse<ApiProductDto> getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);

        String cacheKey = PRODUCT_CACHE_KEY + id;

        try {
            ApiProduct cachedProduct = (ApiProduct) redisTemplate.opsForValue().get(cacheKey);
            if (cachedProduct != null) {
                log.info("Cache HIT: Product {} found in Redis", id);
                return new ApiResponse<>("Product found (from cache)", true,
                        productMapper.toDto(cachedProduct));
            }
        } catch (Exception e) {
            log.warn("Redis cache read failed for product {}: {}", id, e.getMessage());
        }

        log.info("Cache MISS: Fetching product {} from database", id);
        ApiProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        try {
            redisTemplate.opsForValue().set(
                    cacheKey,
                    product,
                    Duration.ofMinutes(CACHE_TTL_MINUTES)
            );
            log.info("Cached product {} in Redis", id);
        } catch (Exception e) {
            log.warn("Failed to cache product {}: {}", id, e.getMessage());
        }

        return new ApiResponse<>("Product found", true, productMapper.toDto(product));
    }

    @Transactional
    public ApiResponse<ApiProductDto> updateProduct(Long id, ApiProductDto updateDto) {
        log.info("Updating product with ID: {}", id);

        ApiProduct existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setName(updateDto.name());
        existingProduct.setEndpoint(updateDto.endpoint());
        existingProduct.setFreeQuota(updateDto.freeQuota());

        ApiProduct updated = productRepository.save(existingProduct);

        String cacheKey = PRODUCT_CACHE_KEY + id;
        try {
            redisTemplate.opsForValue().set(
                    cacheKey,
                    updated,
                    Duration.ofMinutes(CACHE_TTL_MINUTES)
            );
            log.info("Updated product {} in Redis cache", id);
        } catch (Exception e) {
            log.warn("Failed to update cache for product {}: {}", id, e.getMessage());
        }

        try {
            redisTemplate.delete(ALL_PRODUCTS_CACHE_KEY);
            log.info("Invalidated all products cache after update");
        } catch (Exception e) {
            log.warn("Failed to invalidate all products cache: {}", e.getMessage());
        }

        return new ApiResponse<>("Product updated successfully", true,
                productMapper.toDto(updated));
    }

    @Transactional
    public ApiResponse<String> deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);

        String cacheKey = PRODUCT_CACHE_KEY + id;
        try {
            redisTemplate.delete(cacheKey);
            log.info("Removed product {} from Redis cache", id);
        } catch (Exception e) {
            log.warn("Failed to remove product {} from cache: {}", id, e.getMessage());
        }

        try {
            redisTemplate.delete(ALL_PRODUCTS_CACHE_KEY);
            log.info("Invalidated all products cache after deletion");
        } catch (Exception e) {
            log.warn("Failed to invalidate all products cache: {}", e.getMessage());
        }

        return new ApiResponse<>("Product deleted successfully", true, null);
    }
}