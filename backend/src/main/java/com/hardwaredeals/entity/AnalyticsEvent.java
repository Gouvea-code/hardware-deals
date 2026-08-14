package com.hardwaredeals.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analytics_events", indexes = {
        @Index(name = "idx_analytics_events_type_occurred", columnList = "event_type,occurred_at"),
        @Index(name = "idx_analytics_events_user_id", columnList = "user_id"),
        @Index(name = "idx_analytics_events_occurred_at", columnList = "occurred_at")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsEvent {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private AnalyticsEventType eventType;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "offer_id")
    private Offer offer;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime occurredAt = LocalDateTime.now();
}
