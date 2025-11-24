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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiProductServiceImpl implements ApiProductService {

    private final ApiProductRepository productRepository;
    private final ApiItemRepository itemRepository;
    private final ApiProductMapper productMapper;
    private final ApiItemMapper itemMapper;

    @Transactional
    public ApiResponse createProduct(ApiProductCreateDto createDto) {
        ApiProduct product = productMapper.toEntity(createDto);

        ApiProduct savedProduct = productRepository.save(product);

        List<ApiItemCreateDto> itemDtos = createDto.apiItems();
        if (itemDtos != null && !itemDtos.isEmpty()) {
            List<ApiItem> items = itemDtos.stream()
                    .map(itemDto -> {
                        ApiItem item = itemMapper.toEntity(itemDto);
                        item.setApiProduct(savedProduct);
                        return item;
                    })
                    .collect(Collectors.toList());
            itemRepository.saveAll(items);
        }
        return new ApiResponse("Product Created Successfully", true, null);
    }


    public List<ApiProductDto> getAllProducts() {

        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ApiResponse<ApiProductDto> getProductById(Long id) {
        ApiProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        ApiProductDto productDto = productMapper.toDto(product);
        return new ApiResponse<>("Product found", true, productDto);
    }
    @Transactional
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
    public ApiResponse<String> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
        return new ApiResponse("Product deleted successfully", true, null);
    }

}