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

class MercadoLivrePriceCollectorTest {
    @Test void mapsOfficialSearchResults() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        MarketplaceProperties.Channel channel = new MarketplaceProperties.Channel();
        channel.setApiUrl("https://api.mercadolibre.test/sites/MLB/search");
        channel.setQuery("placa de video");
        channel.setAccessToken("access-token");
        server.expect(requestTo("https://api.mercadolibre.test/sites/MLB/search?q=placa%20de%20video&limit=50"))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andRespond(withSuccess("""
                  {"results":[{"id":"MLB123","title":"GPU X","permalink":"https://produto.mercadolivre.com.br/MLB123",
                  "category_id":"MLB1658","price":1999.90,"original_price":2199.90,"available_quantity":1,
                  "attributes":[{"id":"BRAND","value_name":"Marca X"},{"id":"MODEL","value_name":"X"}]}]}
                  """, MediaType.APPLICATION_JSON));
        var offers = new MercadoLivrePriceCollector(builder, channel).collect();
        assertThat(offers).singleElement().satisfies(offer -> {
            assertThat(offer.storeSlug()).isEqualTo("mercado-livre");
            assertThat(offer.externalId()).isEqualTo("MLB123");
            assertThat(offer.brand()).isEqualTo("Marca X");
            assertThat(offer.price()).isEqualByComparingTo("1999.90");
        });
        server.verify();
    }
}
