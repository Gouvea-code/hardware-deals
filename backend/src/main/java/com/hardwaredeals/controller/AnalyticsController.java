package com.hardwaredeals.controller;

import com.hardwaredeals.dto.AnalyticsDtos.*;
import com.hardwaredeals.service.AnalyticsService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics/events")
public class AnalyticsController {
    private final AnalyticsService analytics;
    public AnalyticsController(AnalyticsService analytics) { this.analytics = analytics; }

    @PostMapping
    public ResponseEntity<AnalyticsEventResponse> record(@Valid @RequestBody AnalyticsEventRequest request,
                                                          Authentication authentication) {
        UUID userId = authentication == null ? null : UUID.fromString(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(analytics.record(request, userId));
    }
}
