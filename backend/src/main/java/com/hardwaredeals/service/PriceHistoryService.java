package com.hardwaredeals.service;

import com.hardwaredeals.dto.PriceHistoryDtos.*;
import com.hardwaredeals.entity.PriceHistory;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PriceHistoryService {
    private static final int SCALE = 2;
    private final ProductRepository products;
    private final PriceHistoryRepository history;

    public PriceHistoryService(ProductRepository products, PriceHistoryRepository history) {
        this.products = products; this.history = history;
    }

    public PriceHistoryResponse get(UUID productId) {
        if (!products.existsByIdAndActiveTrue(productId))
            throw new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        List<PriceHistory> entries = history.findByProductIdOrderByCollectedAtAsc(productId);
        if (entries.isEmpty())
            return new PriceHistoryResponse(productId, null, null, null, null, null, null, List.of());

        List<BigDecimal> prices = entries.stream().map(PriceHistory::getPrice).sorted().toList();
        BigDecimal current = entries.get(entries.size() - 1).getPrice().setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal average = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(prices.size()), SCALE, RoundingMode.HALF_UP);
        BigDecimal median = median(prices);
        BigDecimal variation = average.signum() == 0 ? BigDecimal.ZERO.setScale(SCALE)
                : current.subtract(average).multiply(BigDecimal.valueOf(100))
                        .divide(average, SCALE, RoundingMode.HALF_UP);
        List<PricePoint> points = entries.stream().map(entry -> new PricePoint(entry.getStore().getId(),
                entry.getStore().getName(), entry.getPrice().setScale(SCALE, RoundingMode.HALF_UP), entry.getCollectedAt())).toList();
        return new PriceHistoryResponse(productId, current, prices.get(0).setScale(SCALE, RoundingMode.HALF_UP),
                prices.get(prices.size() - 1).setScale(SCALE, RoundingMode.HALF_UP), average, median, variation, points);
    }

    private BigDecimal median(List<BigDecimal> sorted) {
        int middle = sorted.size() / 2;
        if (sorted.size() % 2 == 1) return sorted.get(middle).setScale(SCALE, RoundingMode.HALF_UP);
        return sorted.get(middle - 1).add(sorted.get(middle))
                .divide(BigDecimal.valueOf(2), SCALE, RoundingMode.HALF_UP);
    }
}
