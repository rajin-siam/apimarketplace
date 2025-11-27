package com.siam.apimarketplace.repository;

import com.siam.apimarketplace.entity.ApiItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiItemRepository extends JpaRepository<ApiItem, Long> {
    List<ApiItem> findByApiProductId(Long apiProductId);

    // Find active items by product ID
    List<ApiItem> findByApiProductIdAndIsActiveTrue(Long apiProductId);

    // Find all active items
    List<ApiItem> findByIsActiveTrue();

}