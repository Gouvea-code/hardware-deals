package com.hardwaredeals.collector;

import org.springframework.stereotype.Component;
import java.net.URI;

@Component
public class CollectedOfferValidator {
    public void validate(CollectedOffer offer) {
        require(offer.storeSlug(), "storeSlug"); require(offer.externalId(), "externalId");
        require(offer.sku(), "sku"); require(offer.productName(), "productName");
        require(offer.brand(), "brand"); require(offer.model(), "model");
        require(offer.category(), "category"); require(offer.ean(), "ean"); require(offer.url(), "url");
        if (offer.price() == null || offer.price().signum() <= 0) throw new IllegalArgumentException("price must be positive");
        if (offer.originalPrice() != null && offer.originalPrice().signum() <= 0)
            throw new IllegalArgumentException("originalPrice must be positive");
        URI uri = URI.create(offer.url());
        if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme()))
            throw new IllegalArgumentException("url must use HTTP or HTTPS");
    }

    private void require(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
    }
}
