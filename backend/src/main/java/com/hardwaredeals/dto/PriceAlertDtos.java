package com.hardwaredeals.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class PriceAlertDtos {
    private PriceAlertDtos() {}
    public record SaveAlertRequest(@NotNull @DecimalMin("0.01") BigDecimal targetPrice) {}
    public record AlertResponse(UUID id, UUID productId, String productName, String brand, String imageUrl,
                                BigDecimal targetPrice, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {}
}
