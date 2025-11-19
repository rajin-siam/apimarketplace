package com.siam.apimarketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "api_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiItem {

    @Id
    @SequenceGenerator(
            name = "api_item_seq",
            sequenceName = "api_item_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "api_item_seq"
    )
    private Integer id;

    @NotBlank(message = "Tier name is required")
    @Column(nullable = false)
    private String tierName;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "API product is required")
    @ManyToOne
    @JoinColumn(name = "api_product_id", nullable = false)
    @JsonIgnore
    private ApiProduct apiProduct;
}