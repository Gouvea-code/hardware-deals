package com.hardwaredeals.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "store_products", indexes = {
    @Index(name = "idx_store_products_store_product", columnList = "store_id,product_id"),
    @Index(name = "idx_store_products_external_id", columnList = "external_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String externalName;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
