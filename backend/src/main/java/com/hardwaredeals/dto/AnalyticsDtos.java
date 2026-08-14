package com.hardwaredeals.dto;

import com.hardwaredeals.entity.AnalyticsEventType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public final class AnalyticsDtos {
    private AnalyticsDtos() {}
    public record AnalyticsEventRequest(@NotNull AnalyticsEventType eventType, UUID productId,
                                        UUID notificationId) {}
    public record AnalyticsEventResponse(UUID id, AnalyticsEventType eventType, LocalDateTime occurredAt) {}
}
