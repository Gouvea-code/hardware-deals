package com.hardwaredeals.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "offer_clicks", indexes = {
        @Index(name = "idx_offer_clicks_offer_id", columnList = "offer_id"),
        @Index(name = "idx_offer_clicks_user_id", columnList = "user_id"),
        @Index(name = "idx_offer_clicks_clicked_at", columnList = "clicked_at")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferClick {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Column(name = "clicked_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime clickedAt = LocalDateTime.now();
}
