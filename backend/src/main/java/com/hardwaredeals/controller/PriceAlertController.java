package com.hardwaredeals.controller;

import com.hardwaredeals.dto.PriceAlertDtos.*;
import com.hardwaredeals.service.PriceAlertService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/alerts")
public class PriceAlertController {
    private final PriceAlertService alerts;
    public PriceAlertController(PriceAlertService alerts) { this.alerts = alerts; }
    @GetMapping public List<AlertResponse> list(Authentication auth) { return alerts.list(userId(auth)); }
    @PutMapping("/{productId}") public AlertResponse save(Authentication auth, @PathVariable UUID productId,
            @Valid @RequestBody SaveAlertRequest request) { return alerts.save(userId(auth), productId, request); }
    @DeleteMapping("/{productId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(Authentication auth, @PathVariable UUID productId) { alerts.remove(userId(auth), productId); }
    private UUID userId(Authentication auth) { return UUID.fromString(auth.getName()); }
}
