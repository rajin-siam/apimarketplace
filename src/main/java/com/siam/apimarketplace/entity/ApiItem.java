package com.siam.apimarketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    private String tierName;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "api_product_id", nullable = false)
    @JsonIgnore
    private ApiProduct apiProduct;
}
