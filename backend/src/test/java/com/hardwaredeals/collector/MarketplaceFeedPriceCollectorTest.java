package com.hardwaredeals.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class MarketplaceFeedPriceCollectorTest {
    @Test void authenticatesAndPinsTheConfiguredStore() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        MarketplaceProperties.Channel channel = new MarketplaceProperties.Channel();
        channel.setApiUrl("https://partner.example/offers");
        channel.setAccessToken("secret-token");
        server.expect(requestTo(channel.getApiUrl()))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer secret-token"))
                .andRespond(withSuccess("""
                    [{"storeSlug":"untrusted","externalId":"123","sku":"gpu-123",
                    "productName":"GPU 123","brand":"Brand","model":"123","category":"GPU",
                    "url":"https://partner.example/p/123","price":1000,"available":true}]
                    """, MediaType.APPLICATION_JSON));

        var offers = new MarketplaceFeedPriceCollector("kabum", builder, channel).collect();
        assertThat(offers).singleElement().satisfies(offer -> {
            assertThat(offer.storeSlug()).isEqualTo("kabum");
            assertThat(offer.price()).isEqualByComparingTo("1000");
        });
        server.verify();
    }
}
