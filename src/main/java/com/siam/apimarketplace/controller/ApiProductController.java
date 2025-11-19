package com.siam.apimarketplace.controller;

import com.siam.apimarketplace.dto.ApiProductCreateDto;
import com.siam.apimarketplace.dto.ApiProductDto;
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
    public ResponseEntity<ApiProductDto> createProduct(@Valid @RequestBody ApiProductCreateDto createDto) {
        ApiProductDto created = productService.createProduct(createDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ApiProductDto>> getAllProducts() {
        List<ApiProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiProductDto> getProductById(@PathVariable @Positive(message = "ID must be positive") Integer id) {
        ApiProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiProductDto> updateProduct(
            @PathVariable @Positive(message = "ID must be positive") Integer id,
            @Valid @RequestBody ApiProductDto updateDto) {
        ApiProductDto updated = productService.updateProduct(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Positive(message = "ID must be positive") Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}