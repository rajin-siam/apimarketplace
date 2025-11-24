package com.siam.apimarketplace.controller;

import com.siam.apimarketplace.dto.ApiProductCreateDto;
import com.siam.apimarketplace.dto.ApiProductDto;
import com.siam.apimarketplace.dto.ApiResponse;
import com.siam.apimarketplace.service.ApiProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ApiProductController {

    private final ApiProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ApiProductCreateDto createDto) {
        ApiResponse response = productService.createProduct(createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ApiProductDto>> getAllProducts() {
        List<ApiProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable @Positive(message = "ID must be positive") Long id) {
        ApiResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody ApiProductDto updateDto) {
        ApiResponse response = productService.updateProduct(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Positive(message = "ID must be positive") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}