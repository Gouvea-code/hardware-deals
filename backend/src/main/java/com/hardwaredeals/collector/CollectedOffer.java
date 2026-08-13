package com.hardwaredeals.collector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CollectedOffer(
        String storeSlug, String externalId, String sku, String productName,
        String brand, String model, String category, String ean, String url,
        BigDecimal price, BigDecimal originalPrice, String coupon,
        Boolean available, LocalDateTime collectedAt) {
}
