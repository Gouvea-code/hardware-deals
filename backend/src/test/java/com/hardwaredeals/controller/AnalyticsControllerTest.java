package com.hardwaredeals.controller;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnalyticsControllerTest {
    @Autowired MockMvc mvc;
    @Autowired AnalyticsEventRepository events;
    @Autowired ProductRepository products;

    @AfterEach void clean() { events.deleteAll(); }

    @Test
    void recordsOnlyMinimalAnonymousEvents() throws Exception {
        mvc.perform(post("/api/v1/analytics/events").contentType("application/json")
                        .content("{\"eventType\":\"APP_OPEN\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.eventType").value("APP_OPEN"));
        mvc.perform(post("/api/v1/analytics/events").contentType("application/json")
                        .content("{\"eventType\":\"SEARCH\"}"))
                .andExpect(status().isCreated());
        assertThat(events.countByEventType(AnalyticsEventType.APP_OPEN)).isEqualTo(1);
        assertThat(events.countByEventType(AnalyticsEventType.SEARCH)).isEqualTo(1);
    }

    @Test
    void validatesContextAndProtectsServerOwnedEvent() throws Exception {
        Product product = products.save(Product.builder().name("GPU Analytics").brand("Brand").model("A")
                .category("GPU").ean(java.util.UUID.randomUUID().toString().substring(0,12))
                .normalizedName("gpu analytics").active(true).build());
        mvc.perform(post("/api/v1/analytics/events").contentType("application/json")
                        .content("{\"eventType\":\"PRODUCT_VIEW\",\"productId\":\"" + product.getId() + "\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/v1/analytics/events").contentType("application/json")
                        .content("{\"eventType\":\"SEARCH\",\"productId\":\"" + product.getId() + "\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/analytics/events").contentType("application/json")
                        .content("{\"eventType\":\"FAVORITE\",\"productId\":\"" + product.getId() + "\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/v1/analytics/events").contentType("application/json")
                        .content("{\"eventType\":\"OFFER_CLICK\"}"))
                .andExpect(status().isBadRequest());
    }
}
