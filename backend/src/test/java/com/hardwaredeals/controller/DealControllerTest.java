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
class DealControllerTest {
    @Autowired MockMvc mvc;
    @Autowired OfferRepository offers;
    @Autowired PriceHistoryRepository history;
    @Autowired StoreProductRepository storeProducts;
    @Autowired ProductRepository products;
    @Autowired StoreRepository stores;
    private Product gpu;
    private Offer best;

    @BeforeEach
    void setUp() {
        offers.deleteAll(); history.deleteAll(); storeProducts.deleteAll(); products.deleteAll(); stores.deleteAll();
        Store alpha = stores.save(store("Alpha", "alpha", true));
        Store beta = stores.save(store("Beta", "beta", true));
        gpu = products.save(product("GPU One", "7000000000001", true));
        Product cpu = products.save(product("CPU Two", "7000000000002", true));
        StoreProduct alphaGpu = storeProducts.save(link(alpha, gpu, "alpha-gpu"));
        StoreProduct betaGpu = storeProducts.save(link(beta, gpu, "beta-gpu"));
        StoreProduct alphaCpu = storeProducts.save(link(alpha, cpu, "alpha-cpu"));

        saveHistory(gpu, alpha, "120", 1); saveHistory(gpu, alpha, "100", 2); saveHistory(gpu, alpha, "80", 3);
        saveHistory(cpu, alpha, "200", 1); saveHistory(cpu, alpha, "180", 2);
        offers.save(offer(alphaGpu, "110", "130", 1, true, null));
        best = offers.save(offer(alphaGpu, "80", "120", 3, true, "SAVE"));
        offers.save(offer(betaGpu, "90", "100", 2, true, null));
        offers.save(offer(alphaCpu, "180", "220", 4, true, null));
    }

    @Test
    void listsOnlyLatestOfferPerStoreProductOrderedByScore() throws Exception {
        mvc.perform(get("/api/v1/deals"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(best.getId().toString()))
                .andExpect(jsonPath("$[0].classification").value("EXCELENTE"))
                .andExpect(jsonPath("$[0].discountPercent").value(33.33));
    }

    @Test
    void getsDealAndProductOffers() throws Exception {
        mvc.perform(get("/api/v1/deals/{id}", best.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(gpu.getId().toString()))
                .andExpect(jsonPath("$.storeName").value("Alpha"));
        mvc.perform(get("/api/v1/products/{id}/offers", gpu.getId()).param("sort", "price"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].price").value(80.00)).andExpect(jsonPath("$[1].price").value(90.00));
    }

    @Test
    void supportsDiscountAndRecentOrdering() throws Exception {
        mvc.perform(get("/api/v1/deals").param("sort", "discount"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(best.getId().toString()));
        mvc.perform(get("/api/v1/deals").param("sort", "recent"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].productName").value("CPU Two"));
    }

    @Test
    void rejectsInvalidSortAndMissingResources() throws Exception {
        mvc.perform(get("/api/v1/deals").param("sort", "magic")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/deals/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/products/00000000-0000-0000-0000-000000000000/offers"))
                .andExpect(status().isNotFound());
    }

    @Test
    void hidesUnavailableAndInactiveDeals() throws Exception {
        best.setAvailable(false); offers.save(best);
        mvc.perform(get("/api/v1/deals/{id}", best.getId())).andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/deals")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
    }

    private Store store(String name, String slug, boolean active) {
        return Store.builder().name(name).slug(slug).website("https://" + slug + ".example").active(active).build();
    }
    private Product product(String name, String ean, boolean active) {
        return Product.builder().name(name).brand("Brand").model(name).category("HARDWARE").ean(ean)
                .normalizedName(name.toLowerCase()).active(active).build();
    }
    private StoreProduct link(Store store, Product product, String external) {
        return StoreProduct.builder().store(store).product(product).externalId(external).sku(external.toUpperCase())
                .externalName(product.getName()).url("https://" + store.getSlug() + ".example/" + external).active(true).build();
    }
    private Offer offer(StoreProduct link, String price, String original, int day, boolean available, String coupon) {
        return Offer.builder().storeProduct(link).price(new BigDecimal(price)).originalPrice(new BigDecimal(original))
                .available(available).coupon(coupon).collectedAt(LocalDateTime.of(2026, 8, day, 12, 0)).build();
    }
    private void saveHistory(Product product, Store store, String price, int day) {
        history.save(PriceHistory.builder().product(product).store(store).price(new BigDecimal(price))
                .collectedAt(LocalDateTime.of(2026, 8, day, 10, 0)).build());
    }
}
