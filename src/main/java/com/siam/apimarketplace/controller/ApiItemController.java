package com.siam.apimarketplace.controller;

import com.siam.apimarketplace.dto.ApiItemCreateDto;
import com.siam.apimarketplace.dto.ApiItemDto;

import com.siam.apimarketplace.service.ApiItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
// TODO: add validation inside dtos and entities
public class ApiItemController {

    private final ApiItemService itemService;

    @PostMapping
    public ResponseEntity<ApiItemDto> createItem(@RequestBody ApiItemCreateDto createDto) {
        ApiItemDto created = itemService.createItem(createDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ApiItemDto>> getAllItems() {
        List<ApiItemDto> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiItemDto> getItemById(@PathVariable Integer id) {
        ApiItemDto item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ApiItemDto>> getItemsByProduct(@PathVariable Integer productId) {
        List<ApiItemDto> items = itemService.getItemsByProductId(productId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiItemDto> updateItem(
            @PathVariable Integer id,
            @RequestBody ApiItemDto updateDto) {
        ApiItemDto updated = itemService.updateItem(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}