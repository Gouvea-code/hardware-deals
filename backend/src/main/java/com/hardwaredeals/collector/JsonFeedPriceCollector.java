package com.hardwaredeals.collector;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.net.URI;
import java.util.*;

@Component
@ConditionalOnProperty(prefix = "app.collector", name = "enabled", havingValue = "true")
public class JsonFeedPriceCollector implements PriceCollector {
    private final RestClient restClient;
    private final URI feedUri;

    public JsonFeedPriceCollector(RestClient.Builder builder, CollectorProperties properties) {
        if (properties.getFeedUrl() == null || properties.getFeedUrl().isBlank())
            throw new IllegalStateException("COLLECTOR_FEED_URL is required when the collector is enabled");
        this.feedUri = URI.create(properties.getFeedUrl());
        if (!Set.of("http", "https").contains(feedUri.getScheme()))
            throw new IllegalStateException("Collector feed URL must use HTTP or HTTPS");
        this.restClient = builder.build();
    }

    @Override public String sourceName() { return "configured-json-feed"; }

    @Override
    public List<CollectedOffer> collect() {
        CollectedOffer[] response = restClient.get().uri(feedUri).retrieve().body(CollectedOffer[].class);
        return response == null ? List.of() : List.of(response);
    }
}
