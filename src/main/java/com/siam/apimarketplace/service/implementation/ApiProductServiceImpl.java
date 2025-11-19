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
    public ApiProductDto createProduct(ApiProductCreateDto createDto) {
        ApiProduct product = productMapper.toEntity(createDto);

        ApiProduct savedProduct = productRepository.save(product);

        List<ApiItemCreateDto> itemDtos = createDto.apiItems();
        if (itemDtos != null && itemDtos.size() > 0) {
            List<ApiItem> items = itemDtos.stream()
                    .map(itemDto -> {
                        ApiItem item = itemMapper.toEntity(itemDto);
                        item.setApiProduct(savedProduct);
                        return item;
                    })
                    .collect(Collectors.toList());
            itemRepository.saveAll(items);
            savedProduct.setApiItems(items);
        }
        return productMapper.toDto(savedProduct);
    }


    public List<ApiProductDto> getAllProducts() {

        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ApiProductDto getProductById(Integer id) {
        ApiProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return productMapper.toDto(product);
    }
    //TODO: improving exception handler
    @Transactional
    public ApiProductDto updateProduct(Integer id, ApiProductDto updateDto) {
        ApiProduct existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setName(updateDto.name());
        existingProduct.setEndpoint(updateDto.endpoint());
        existingProduct.setFreeQuota(updateDto.freeQuota());

        ApiProduct updated = productRepository.save(existingProduct);
        return productMapper.toDto(updated);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

}