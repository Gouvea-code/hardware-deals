package com.hardwaredeals.controller;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ProductRepository products;
    @Autowired StoreRepository stores;
    @Autowired StoreProductRepository storeProducts;
    @Autowired OfferRepository offers;
    private Product gpu;
    private Product monitor;

    @BeforeEach
    void setUp() {
        offers.deleteAll();
        storeProducts.deleteAll();
        products.deleteAll();
        stores.deleteAll();
        Store store = stores.save(Store.builder().name("Loja Teste").slug("loja-teste")
                .website("https://example.com").active(true).build());
        gpu = products.save(product("RTX 5070", "Nvidia", "GPU", "111", true));
        monitor = products.save(product("Monitor Ultra", "Acme", "MONITOR", "222", true));
        products.save(product("Produto Inativo", "Acme", "GPU", "333", false));
        addOffer(store, gpu, "3999.90");
        addOffer(store, monitor, "1299.90");
    }

    @Test
    void listsActiveProductsWithStablePaginationAndSorting() throws Exception {
        mvc.perform(get("/api/v1/products").param("size", "1").param("sort", "name_desc"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("RTX 5070"))
                .andExpect(jsonPath("$.totalElements").value(2)).andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void getsProductAndHidesInactiveOrUnknownProducts() throws Exception {
        mvc.perform(get("/api/v1/products/{id}", gpu.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Nvidia"));
        mvc.perform(get("/api/v1/products/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchesAndCombinesCategoryBrandPriceAndStoreFilters() throws Exception {
        mvc.perform(get("/api/v1/products/search").param("q", "rtx").param("category", "gpu")
                        .param("brand", "nvidia").param("minPrice", "3000").param("maxPrice", "4500")
                        .param("store", "loja-teste"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(gpu.getId().toString()));
        mvc.perform(get("/api/v1/products").param("maxPrice", "1500"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(monitor.getId().toString()));
    }

    @Test
    void validatesSearchPaginationPricesSortAndIdentifier() throws Exception {
        mvc.perform(get("/api/v1/products/search").param("q", " ")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/products").param("size", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/products").param("minPrice", "20").param("maxPrice", "10"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/products").param("sort", "price_magic")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/products/not-a-uuid")).andExpect(status().isBadRequest());
    }

    private Product product(String name, String brand, String category, String ean, boolean active) {
        return Product.builder().name(name).brand(brand).model(name).category(category).ean(ean)
                .normalizedName(name.toLowerCase()).active(active).build();
    }

    private void addOffer(Store store, Product product, String price) {
        StoreProduct sp = storeProducts.save(StoreProduct.builder().store(store).product(product)
                .externalId("ext-" + product.getEan()).sku("sku-" + product.getEan())
                .externalName(product.getName()).url("https://example.com/" + product.getEan()).active(true).build());
        offers.save(Offer.builder().storeProduct(sp).price(new BigDecimal(price))
                .originalPrice(new BigDecimal(price)).available(true).build());
    }
}
