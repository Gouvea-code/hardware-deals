package com.hardwaredeals.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_products_ean", columnList = "ean", unique = true),
    @Index(name = "idx_products_category", columnList = "category"),
    @Index(name = "idx_products_brand_normalized_name", columnList = "brand,normalized_name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, unique = true)
    private String ean;

    @Column(nullable = false)
    private String normalizedName;

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
