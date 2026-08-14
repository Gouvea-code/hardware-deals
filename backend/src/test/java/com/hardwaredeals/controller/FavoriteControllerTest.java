package com.hardwaredeals.controller;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class FavoriteControllerTest {
    @Autowired MockMvc mvc; @Autowired FavoriteRepository favorites; @Autowired ProductRepository products; @Autowired UserRepository users;
    private User user; private Product product;
    @BeforeEach void setUp() {
        favorites.deleteAll();
        user = users.save(User.builder().name("User").email("favorite-" + java.util.UUID.randomUUID() + "@example.com")
                .passwordHash("hash").emailVerified(true).build());
        product = products.save(Product.builder().name("RTX").brand("Nvidia").model("5070").category("GPU")
                .ean(String.valueOf(Math.abs(java.util.UUID.randomUUID().getMostSignificantBits())))
                .normalizedName("rtx").active(true).build());
    }
    @Test void addsListsAndRemovesFavoriteIdempotently() throws Exception {
        mvc.perform(put("/api/v1/favorites/{id}", product.getId()).with(authentication(auth())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.productName").value("RTX"));
        mvc.perform(put("/api/v1/favorites/{id}", product.getId()).with(authentication(auth()))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/favorites").with(authentication(auth())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(delete("/api/v1/favorites/{id}", product.getId()).with(authentication(auth()))).andExpect(status().isNoContent());
        Assertions.assertEquals(0, favorites.count());
    }
    @Test void protectsEndpointsAndRejectsUnknownProduct() throws Exception {
        mvc.perform(get("/api/v1/favorites")).andExpect(status().isForbidden());
        mvc.perform(put("/api/v1/favorites/00000000-0000-0000-0000-000000000000").with(authentication(auth())))
                .andExpect(status().isNotFound());
    }
    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(user.getId().toString(), null, List.of());
    }
}
