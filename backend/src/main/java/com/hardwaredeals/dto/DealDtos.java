package com.hardwaredeals.dto;

import com.hardwaredeals.deal.DealClassification;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public final class DealDtos {
    private DealDtos() {}

    public record DealResponse(UUID id, UUID productId, String productName, String brand, String imageUrl,
                               UUID storeId, String storeName, BigDecimal price, BigDecimal originalPrice,
                               BigDecimal discountPercent, String coupon, boolean available, String url,
                               LocalDateTime collectedAt, int score, DealClassification classification) {}
}
