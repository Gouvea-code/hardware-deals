package com.hardwaredeals.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public final class PriceHistoryDtos {
    private PriceHistoryDtos() {}

    public record PricePoint(UUID storeId, String storeName, BigDecimal price, LocalDateTime collectedAt) {}

    public record PriceHistoryResponse(UUID productId, BigDecimal currentPrice, BigDecimal lowestPrice,
                                       BigDecimal highestPrice, BigDecimal averagePrice, BigDecimal medianPrice,
                                       BigDecimal priceVariation, List<PricePoint> history) {}
}
