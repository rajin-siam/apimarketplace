package com.siam.apimarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Endpoint is required")
    @Column(nullable = false)
    private String endpoint;

    @NotNull(message = "Free quota is required")
    @PositiveOrZero(message = "Free quota must be zero or positive")
    @Column(nullable = false)
    private Double freeQuota;

    @OneToMany(mappedBy = "apiProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApiItem> apiItems;
}