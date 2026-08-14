package com.hardwaredeals.collector;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

public class MercadoLivrePriceCollector implements PriceCollector {
    private final RestClient client;
    private final URI searchUri;

    public MercadoLivrePriceCollector(RestClient.Builder builder, MarketplaceProperties.Channel channel) {
        if (channel.getAccessToken() == null || channel.getAccessToken().isBlank())
            throw new IllegalStateException("Mercado Livre access token is required when enabled");
        String baseUrl = channel.getApiUrl() == null || channel.getApiUrl().isBlank()
                ? "https://api.mercadolibre.com/sites/MLB/search" : channel.getApiUrl();
        searchUri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("q", channel.getQuery()).queryParam("limit", 50).build().encode().toUri();
        client = builder.clone().defaultHeader(HttpHeaders.AUTHORIZATION,
                "Bearer " + channel.getAccessToken()).build();
    }

    @Override public String sourceName() { return "mercado-livre-official-api"; }

    @Override public List<CollectedOffer> collect() {
        JsonNode response = client.get().uri(searchUri).retrieve().body(JsonNode.class);
        if (response == null || !response.path("results").isArray()) return List.of();
        List<CollectedOffer> offers = new ArrayList<>();
        for (JsonNode item : response.path("results")) {
            String id = text(item, "id");
            String title = text(item, "title");
            String url = text(item, "permalink");
            BigDecimal price = decimal(item, "price");
            if (id == null || title == null || url == null || price == null) continue;
            offers.add(new CollectedOffer("mercado-livre", id, id, title,
                    attribute(item, "BRAND"), attribute(item, "MODEL"), text(item, "category_id"),
                    attribute(item, "GTIN"), url, price, decimal(item, "original_price"), null,
                    item.path("available_quantity").asInt(0) > 0, LocalDateTime.now()));
        }
        return offers;
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() || value.asText().isBlank() ? null : value.asText();
    }
    private static BigDecimal decimal(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || !value.isNumber() ? null : value.decimalValue();
    }
    private static String attribute(JsonNode item, String id) {
        for (JsonNode value : item.path("attributes"))
            if (id.equals(value.path("id").asText())) return text(value, "value_name");
        return null;
    }
}
