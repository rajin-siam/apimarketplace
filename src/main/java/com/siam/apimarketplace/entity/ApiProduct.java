package com.siam.apimarketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;


@Entity
@Table(name = "api_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE api_product SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown JSON properties
public class ApiProduct extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Long id;

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
}