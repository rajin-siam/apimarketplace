package com.siam.apimarketplace.repository;

import com.siam.apimarketplace.entity.ApiProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApiProductRepository extends JpaRepository<ApiProduct, Long> {
    List<ApiProduct> findByIsActiveTrue();
}
