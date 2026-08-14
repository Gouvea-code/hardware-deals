package com.hardwaredeals.repository;

import com.hardwaredeals.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, UUID> {
    long countByEventType(AnalyticsEventType eventType);
    long deleteByOccurredAtBefore(LocalDateTime cutoff);
    void deleteByUserId(UUID userId);
}
