package com.hardwaredeals.collector;

import java.net.URI;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

/** Reads a normalized, contract-authorized partner API without scraping storefront HTML. */
public class MarketplaceFeedPriceCollector implements PriceCollector {
    private final String storeSlug;
    private final RestClient client;
    private final URI uri;

    public MarketplaceFeedPriceCollector(String storeSlug, RestClient.Builder builder,
                                         MarketplaceProperties.Channel channel) {
        this.storeSlug = storeSlug;
        if (channel.getApiUrl() == null || channel.getApiUrl().isBlank())
            throw new IllegalStateException(storeSlug + " API URL is required when enabled");
        uri = URI.create(channel.getApiUrl());
        if (!Set.of("http", "https").contains(uri.getScheme()))
            throw new IllegalStateException(storeSlug + " API URL must use HTTP or HTTPS");
        RestClient.Builder configured = builder.clone();
        if (channel.getAccessToken() != null && !channel.getAccessToken().isBlank())
            configured.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + channel.getAccessToken());
        client = configured.build();
    }

    @Override public String sourceName() { return storeSlug + "-authorized-api"; }

    @Override public List<CollectedOffer> collect() {
        CollectedOffer[] body = client.get().uri(uri).retrieve().body(CollectedOffer[].class);
        if (body == null) return List.of();
        return List.of(body).stream().map(value -> new CollectedOffer(
                storeSlug, value.externalId(), value.sku(), value.productName(), value.brand(),
                value.model(), value.category(), value.ean(), value.url(), value.price(),
                value.originalPrice(), value.coupon(), value.available(), value.collectedAt())).toList();
    }
}
