package com.hardwaredeals.collector;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MarketplaceCollectorConfiguration {
    @Bean("amazonBrasilPriceCollector")
    @ConditionalOnProperty(prefix="app.marketplaces.amazon-brasil", name="enabled", havingValue="true")
    PriceCollector amazon(RestClient.Builder builder, MarketplaceProperties properties) {
        return new MarketplaceFeedPriceCollector("amazon-brasil", builder, properties.getAmazonBrasil());
    }

    @Bean("kabumPriceCollector")
    @ConditionalOnProperty(prefix="app.marketplaces.kabum", name="enabled", havingValue="true")
    PriceCollector kabum(RestClient.Builder builder, MarketplaceProperties properties) {
        return new MarketplaceFeedPriceCollector("kabum", builder, properties.getKabum());
    }

    @Bean("magazineLuizaPriceCollector")
    @ConditionalOnProperty(prefix="app.marketplaces.magazine-luiza", name="enabled", havingValue="true")
    PriceCollector magazineLuiza(RestClient.Builder builder, MarketplaceProperties properties) {
        return new MarketplaceFeedPriceCollector("magazine-luiza", builder, properties.getMagazineLuiza());
    }

    @Bean("mercadoLivrePriceCollector")
    @ConditionalOnProperty(prefix="app.marketplaces.mercado-livre", name="enabled", havingValue="true")
    PriceCollector mercadoLivre(RestClient.Builder builder, MarketplaceProperties properties) {
        return new MercadoLivrePriceCollector(builder, properties.getMercadoLivre());
    }
}
