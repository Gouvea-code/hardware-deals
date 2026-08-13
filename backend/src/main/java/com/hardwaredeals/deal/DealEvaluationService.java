package com.hardwaredeals.deal;

import org.springframework.stereotype.Service;
import java.math.*;

@Service
public class DealEvaluationService {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public DealEvaluation evaluate(DealEvaluationInput input) {
        validate(input);
        if (!input.available()) return result(0);

        BigDecimal discountPercent = input.averagePrice().subtract(input.currentPrice())
                .multiply(ONE_HUNDRED).divide(input.averagePrice(), 4, RoundingMode.HALF_UP);
        BigDecimal averageComponent = clamp(discountPercent.multiply(BigDecimal.valueOf(2)), -50, 35);
        BigDecimal historicalComponent = historicalPosition(input);
        BigDecimal couponComponent = hasCoupon(input.coupon()) ? BigDecimal.valueOf(5) : BigDecimal.ZERO;
        int score = BigDecimal.valueOf(50).add(averageComponent).add(historicalComponent).add(couponComponent)
                .setScale(0, RoundingMode.HALF_UP).intValue();
        return result(Math.max(0, Math.min(100, score)));
    }

    public DealClassification classify(int score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("score must be between 0 and 100");
        if (score >= 90) return DealClassification.EXCELENTE;
        if (score >= 80) return DealClassification.OTIMA;
        if (score >= 70) return DealClassification.BOA;
        if (score >= 60) return DealClassification.INTERESSANTE;
        return DealClassification.NORMAL;
    }

    private BigDecimal historicalPosition(DealEvaluationInput input) {
        if (input.currentPrice().compareTo(input.lowestPrice()) <= 0) return BigDecimal.TEN;
        BigDecimal range = input.highestPrice().subtract(input.lowestPrice());
        if (range.signum() == 0) return BigDecimal.ZERO;
        return clamp(input.highestPrice().subtract(input.currentPrice()).multiply(BigDecimal.TEN)
                .divide(range, 4, RoundingMode.HALF_UP), 0, 10);
    }

    private void validate(DealEvaluationInput input) {
        if (input == null) throw new IllegalArgumentException("input is required");
        positive(input.currentPrice(), "currentPrice"); positive(input.averagePrice(), "averagePrice");
        positive(input.lowestPrice(), "lowestPrice"); positive(input.highestPrice(), "highestPrice");
        if (input.lowestPrice().compareTo(input.highestPrice()) > 0)
            throw new IllegalArgumentException("lowestPrice cannot exceed highestPrice");
    }

    private void positive(BigDecimal value, String field) {
        if (value == null || value.signum() <= 0) throw new IllegalArgumentException(field + " must be positive");
    }
    private BigDecimal clamp(BigDecimal value, int minimum, int maximum) {
        return value.max(BigDecimal.valueOf(minimum)).min(BigDecimal.valueOf(maximum));
    }
    private boolean hasCoupon(String coupon) { return coupon != null && !coupon.isBlank(); }
    private DealEvaluation result(int score) { return new DealEvaluation(score, classify(score)); }
}
