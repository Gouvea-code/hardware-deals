package com.hardwaredeals.controller;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OfferRedirectControllerTest {
    @Autowired MockMvc mvc;
    @Autowired OfferClickRepository clicks;
    @Autowired AnalyticsEventRepository analyticsEvents;
    @Autowired OfferRepository offers;
    @Autowired StoreProductRepository storeProducts;
    @Autowired ProductRepository products;
    @Autowired StoreRepository stores;

    @AfterEach void clean() { analyticsEvents.deleteAll(); clicks.deleteAll(); }

    @Test
    void recordsAnonymousClickAndReturnsTrustedDestination() throws Exception {
        Offer offer = createOffer("https://shop.example/item/123", "https://shop.example");
        mvc.perform(post("/api/v1/offers/{id}/click", offer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clickId").isNotEmpty())
                .andExpect(jsonPath("$.redirectUrl").value("https://shop.example/item/123"));
        assertThat(clicks.countByOfferId(offer.getId())).isEqualTo(1);
        assertThat(analyticsEvents.countByEventType(AnalyticsEventType.OFFER_CLICK)).isEqualTo(1);
    }

    @Test
    void refusesExternalHostAndUnavailableOffer() throws Exception {
        Offer unsafe = createOffer("https://attacker.example/item", "https://shop.example");
        mvc.perform(post("/api/v1/offers/{id}/click", unsafe.getId()))
                .andExpect(status().isUnprocessableEntity());
        unsafe.setAvailable(false); offers.save(unsafe);
        mvc.perform(post("/api/v1/offers/{id}/click", unsafe.getId())).andExpect(status().isNotFound());
        assertThat(clicks.count()).isZero();
    }

    private Offer createOffer(String url, String website) {
        Store store = stores.save(Store.builder().name("Shop").slug("shop-" + java.util.UUID.randomUUID())
                .website(website).active(true).build());
        Product product = products.save(Product.builder().name("GPU").brand("Brand").model("X")
                .category("GPU").ean(java.util.UUID.randomUUID().toString().substring(0, 12))
                .normalizedName("gpu").active(true).build());
        StoreProduct link = storeProducts.save(StoreProduct.builder().store(store).product(product)
                .externalId("ext").sku("sku").externalName("GPU").url(url).active(true).build());
        return offers.save(Offer.builder().storeProduct(link).price(new BigDecimal("100"))
                .originalPrice(new BigDecimal("120")).available(true).build());
    }
}
