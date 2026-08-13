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
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceHistoryControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ProductRepository products;
    @Autowired StoreRepository stores;
    @Autowired PriceHistoryRepository history;
    @Autowired OfferRepository offers;
    @Autowired StoreProductRepository storeProducts;
    private Product product;
    private Store store;

    @BeforeEach
    void setUp() {
        offers.deleteAll(); history.deleteAll(); storeProducts.deleteAll(); products.deleteAll(); stores.deleteAll();
        store = stores.save(Store.builder().name("History Store").slug("history-store")
                .website("https://history.example").active(true).build());
        product = products.save(Product.builder().name("History GPU").brand("Brand").model("Model")
                .category("GPU").ean("9000000000001").normalizedName("history gpu").active(true).build());
    }

    @Test
    void returnsChronologicalHistoryAndStatisticsForOddSample() throws Exception {
        save("100.00", 1); save("200.00", 2); save("150.00", 3);
        mvc.perform(get("/api/v1/products/{id}/price-history", product.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.currentPrice").value(150.00))
                .andExpect(jsonPath("$.lowestPrice").value(100.00)).andExpect(jsonPath("$.highestPrice").value(200.00))
                .andExpect(jsonPath("$.averagePrice").value(150.00)).andExpect(jsonPath("$.medianPrice").value(150.00))
                .andExpect(jsonPath("$.priceVariation").value(0.00))
                .andExpect(jsonPath("$.history[0].price").value(100.00))
                .andExpect(jsonPath("$.history[2].price").value(150.00));
    }

    @Test
    void calculatesEvenMedianAndNegativeVariationFromAverage() throws Exception {
        save("100.00", 1); save("200.00", 2); save("100.00", 3); save("100.00", 4);
        mvc.perform(get("/api/v1/products/{id}/price-history", product.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.currentPrice").value(100.00))
                .andExpect(jsonPath("$.averagePrice").value(125.00)).andExpect(jsonPath("$.medianPrice").value(100.00))
                .andExpect(jsonPath("$.priceVariation").value(-20.00));
    }

    @Test
    void returnsEmptyStatisticsForProductWithoutHistory() throws Exception {
        mvc.perform(get("/api/v1/products/{id}/price-history", product.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.currentPrice").doesNotExist())
                .andExpect(jsonPath("$.history.length()").value(0));
    }

    @Test
    void returnsNotFoundForUnknownOrInactiveProduct() throws Exception {
        product.setActive(false); products.save(product);
        mvc.perform(get("/api/v1/products/{id}/price-history", product.getId())).andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/products/00000000-0000-0000-0000-000000000000/price-history"))
                .andExpect(status().isNotFound());
    }

    private void save(String price, int day) {
        history.save(PriceHistory.builder().product(product).store(store).price(new BigDecimal(price))
                .collectedAt(LocalDateTime.of(2026, 8, day, 12, 0)).build());
    }
}
