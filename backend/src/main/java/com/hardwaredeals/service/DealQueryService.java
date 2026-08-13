package com.hardwaredeals.service;

import com.hardwaredeals.deal.*;
import com.hardwaredeals.dto.DealDtos.DealResponse;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DealQueryService {
    private final OfferRepository offers;
    private final ProductRepository products;
    private final PriceHistoryRepository history;
    private final DealEvaluationService evaluator;

    public DealQueryService(OfferRepository offers, ProductRepository products,
                            PriceHistoryRepository history, DealEvaluationService evaluator) {
        this.offers = offers; this.products = products; this.history = history; this.evaluator = evaluator;
    }

    public List<DealResponse> findDeals(String sort) {
        List<Offer> latest = latestPerStoreProduct(offers.findAllByOrderByCollectedAtDesc());
        return sort(latest.stream().filter(this::isVisibleDeal).map(this::toResponse).toList(), sort);
    }

    public DealResponse findById(UUID id) {
        return offers.findById(id).filter(this::isVisibleDeal).map(this::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Oferta não encontrada"));
    }

    public List<DealResponse> findByProduct(UUID productId, String sort) {
        if (!products.existsByIdAndActiveTrue(productId))
            throw new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        List<Offer> latest = latestPerStoreProduct(offers.findByStoreProductProductIdOrderByCollectedAtDesc(productId));
        return sort(latest.stream().filter(this::isVisibleDeal).map(this::toResponse).toList(), sort);
    }

    private DealResponse toResponse(Offer offer) {
        Product product = offer.getStoreProduct().getProduct();
        Store store = offer.getStoreProduct().getStore();
        List<BigDecimal> prices = history.findByProductIdOrderByCollectedAtAsc(product.getId()).stream()
                .map(PriceHistory::getPrice).toList();
        BigDecimal lowest = prices.stream().min(BigDecimal::compareTo).orElse(offer.getPrice());
        BigDecimal highest = prices.stream().max(BigDecimal::compareTo).orElse(offer.getPrice());
        BigDecimal average = prices.isEmpty() ? offer.getPrice() : prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_UP);
        DealEvaluation evaluation = evaluator.evaluate(new DealEvaluationInput(offer.getPrice(), average, lowest,
                highest, Boolean.TRUE.equals(offer.getAvailable()), offer.getCoupon()));
        BigDecimal discount = offer.getOriginalPrice().signum() == 0 ? BigDecimal.ZERO.setScale(2)
                : offer.getOriginalPrice().subtract(offer.getPrice()).multiply(BigDecimal.valueOf(100))
                        .divide(offer.getOriginalPrice(), 2, RoundingMode.HALF_UP).max(BigDecimal.ZERO.setScale(2));
        return new DealResponse(offer.getId(), product.getId(), product.getName(), product.getBrand(), product.getImageUrl(),
                store.getId(), store.getName(), offer.getPrice(), offer.getOriginalPrice(), discount, offer.getCoupon(),
                Boolean.TRUE.equals(offer.getAvailable()), offer.getStoreProduct().getUrl(), offer.getCollectedAt(),
                evaluation.score(), evaluation.classification());
    }

    private List<DealResponse> sort(List<DealResponse> values, String sort) {
        Comparator<DealResponse> comparator = switch (sort == null ? "score" : sort.toLowerCase(Locale.ROOT)) {
            case "score" -> Comparator.comparingInt(DealResponse::score).reversed()
                    .thenComparing(DealResponse::collectedAt, Comparator.reverseOrder());
            case "price" -> Comparator.comparing(DealResponse::price).thenComparing(DealResponse::productName);
            case "discount" -> Comparator.comparing(DealResponse::discountPercent).reversed()
                    .thenComparing(DealResponse::score, Comparator.reverseOrder());
            case "recent" -> Comparator.comparing(DealResponse::collectedAt).reversed();
            default -> throw new ApiException(HttpStatus.BAD_REQUEST, "Ordenação inválida");
        };
        return values.stream().sorted(comparator).toList();
    }

    private List<Offer> latestPerStoreProduct(List<Offer> values) {
        return new ArrayList<>(values.stream().collect(Collectors.toMap(
                offer -> offer.getStoreProduct().getId(), Function.identity(), (first, ignored) -> first,
                LinkedHashMap::new)).values());
    }

    private boolean isVisibleDeal(Offer offer) {
        StoreProduct sp = offer.getStoreProduct();
        return Boolean.TRUE.equals(offer.getAvailable()) && Boolean.TRUE.equals(sp.getActive())
                && Boolean.TRUE.equals(sp.getProduct().getActive()) && Boolean.TRUE.equals(sp.getStore().getActive());
    }
}
