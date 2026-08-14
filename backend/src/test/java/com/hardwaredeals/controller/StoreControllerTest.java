package com.hardwaredeals.controller;

import com.hardwaredeals.entity.Store;
import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreControllerTest {
    @Autowired MockMvc mvc;
    @Autowired StoreRepository stores;
    @Autowired OfferRepository offers;
    @Autowired StoreProductRepository storeProducts;
    private Store active;
    private Store inactive;

    @BeforeEach
    void setUp() {
        offers.deleteAll();
        storeProducts.deleteAll();
        stores.deleteAll();
        active = stores.save(store("Alpha Store", "alpha-store", true));
        stores.save(store("Beta Store", "beta-store", true));
        inactive = stores.save(store("Hidden Store", "hidden-store", false));
    }

    @Test
    void listsOnlyActiveStoresSortedByName() throws Exception {
        mvc.perform(get("/api/v1/stores")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alpha Store"))
                .andExpect(jsonPath("$[1].name").value("Beta Store"));
    }

    @Test
    void getsActiveStoreById() throws Exception {
        mvc.perform(get("/api/v1/stores/{id}", active.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("alpha-store"))
                .andExpect(jsonPath("$.website").value("https://alpha-store.example"));
    }

    @Test
    void hidesInactiveAndUnknownStoresAndValidatesUuid() throws Exception {
        mvc.perform(get("/api/v1/stores/{id}", inactive.getId())).andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/stores/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/stores/not-a-uuid")).andExpect(status().isBadRequest());
    }

    private Store store(String name, String slug, boolean active) {
        return Store.builder().name(name).slug(slug).website("https://" + slug + ".example").active(active).build();
    }
}
