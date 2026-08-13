package com.hardwaredeals.deal;

import java.math.BigDecimal;

public record DealEvaluationInput(BigDecimal currentPrice, BigDecimal averagePrice,
                                  BigDecimal lowestPrice, BigDecimal highestPrice,
                                  boolean available, String coupon) {
}
