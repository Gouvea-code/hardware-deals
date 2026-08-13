package com.hardwaredeals.collector;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import com.hardwaredeals.normalization.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class CollectedOfferProcessor {
    private final CollectedOfferValidator validator;
    private final StoreRepository stores;
    private final ProductRepository products;
    private final StoreProductRepository storeProducts;
    private final OfferRepository offers;
    private final PriceHistoryRepository history;
    private final ProductNormalizer normalizer;
    private final ProductMatchingService matcher;

    public CollectedOfferProcessor(CollectedOfferValidator validator, StoreRepository stores,
                                   ProductRepository products, StoreProductRepository storeProducts,
                                   OfferRepository offers, PriceHistoryRepository history,
                                   ProductNormalizer normalizer, ProductMatchingService matcher) {
        this.validator = validator; this.stores = stores; this.products = products;
        this.storeProducts = storeProducts; this.offers = offers; this.history = history;
        this.normalizer = normalizer; this.matcher = matcher;
    }

    @Transactional
    public void process(CollectedOffer collected) {
        validator.validate(collected);
        Store store = stores.findBySlug(collected.storeSlug().trim().toLowerCase(Locale.ROOT))
                .filter(s -> Boolean.TRUE.equals(s.getActive()))
                .orElseThrow(() -> new IllegalArgumentException("Active store not found: " + collected.storeSlug()));
        NormalizedProductIdentity identity = normalizer.normalize(collected);
        Product product = matcher.findMatch(identity).orElseGet(() -> products.save(Product.builder()
                .name(collected.productName().trim()).brand(collected.brand().trim()).model(collected.model().trim())
                .category(collected.category().trim()).ean(identity.ean())
                .normalizedName(identity.normalizedName()).active(true).build()));
        StoreProduct storeProduct = storeProducts.findByStoreIdAndProductId(store.getId(), product.getId())
                .orElseGet(() -> storeProducts.save(StoreProduct.builder().store(store).product(product)
                        .externalId(collected.externalId().trim()).sku(identity.sku())
                        .externalName(collected.productName().trim()).url(collected.url().trim()).active(true).build()));

        BigDecimal originalPrice = collected.originalPrice() == null ? collected.price() : collected.originalPrice();
        LocalDateTime collectedAt = collected.collectedAt() == null ? LocalDateTime.now() : collected.collectedAt();
        offers.save(Offer.builder().storeProduct(storeProduct).price(collected.price()).originalPrice(originalPrice)
                .coupon(blankToNull(collected.coupon())).available(collected.available() == null || collected.available())
                .collectedAt(collectedAt).build());
        history.save(PriceHistory.builder().product(product).store(store).price(collected.price())
                .collectedAt(collectedAt).build());
    }

    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
