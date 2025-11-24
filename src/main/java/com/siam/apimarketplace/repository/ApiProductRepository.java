package com.siam.apimarketplace.repository;

import com.siam.apimarketplace.entity.ApiProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiProductRepository extends JpaRepository<ApiProduct, Long> {
}
