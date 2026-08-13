package com.hardwaredeals.collector;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class JsonFeedPriceCollectorTest {
    @Test
    void readsTheConfiguredJsonFeed() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CollectorProperties properties = new CollectorProperties();
        properties.setFeedUrl("https://feed.example/offers.json");
        server.expect(once(), requestTo(properties.getFeedUrl())).andRespond(withSuccess("""
                [{"storeSlug":"source-store","externalId":"abc","sku":"sku-abc",
                  "productName":"GPU ABC","brand":"Brand","model":"ABC","category":"GPU",
                  "ean":"7890000000000","url":"https://store.example/abc","price":1999.90,
                  "originalPrice":2199.90,"available":true}]
                """, MediaType.APPLICATION_JSON));

        JsonFeedPriceCollector collector = new JsonFeedPriceCollector(builder, properties);
        assertThat(collector.collect()).singleElement().satisfies(offer -> {
            assertThat(offer.externalId()).isEqualTo("abc");
            assertThat(offer.price()).isEqualByComparingTo("1999.90");
        });
        server.verify();
    }
}
