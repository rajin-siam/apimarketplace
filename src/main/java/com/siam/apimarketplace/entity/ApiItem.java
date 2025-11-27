package com.siam.apimarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "api_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE api_item SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class ApiItem extends BaseEntity{

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
    private Long id;

    @NotBlank(message = "Tier name is required")
    @Column(nullable = false)
    private String tierName;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "API product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_product_id", nullable = false)
    private ApiProduct apiProduct;

    @Column(name = "api_product_id", insertable = false, updatable = false)
    private Long apiProductId;
}