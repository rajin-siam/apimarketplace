package com.siam.apimarketplace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "api_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiProduct {

    @Id
    @SequenceGenerator(
            name = "api_product_seq",
            sequenceName = "api_product_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "api_product_seq"
    )
    private Integer id;

    private String name;
    private String endpoint;
    private Double freeQuota;

    @OneToMany(mappedBy = "apiProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApiItem> apiItems;
}
