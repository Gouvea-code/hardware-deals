package com.hardwaredeals.api;

import com.hardwaredeals.entity.Product;
import com.hardwaredeals.entity.User;
import com.hardwaredeals.repository.PriceAlertRepository;
import com.hardwaredeals.repository.ProductRepository;
import com.hardwaredeals.repository.UserRepository;
import com.hardwaredeals.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NegativeApiTest {

    private static final String TEST_SECRET = "test-only-secret-with-at-least-32-bytes-long";

    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired ProductRepository products;
    @Autowired PriceAlertRepository alerts;
    @Autowired PasswordEncoder passwords;

    private User owner;
    private User otherUser;
    private Product product;
    private JwtService jwt;

    @BeforeEach
    void setUp() {
        owner = saveUser("owner");
        otherUser = saveUser("other");
        product = products.save(Product.builder()
                .name("Negative Test GPU").brand("Test").model("N1").category("GPU")
                .ean(UUID.randomUUID().toString()).normalizedName("negative test gpu").active(true).build());
        jwt = new JwtService(TEST_SECRET, Duration.ofMinutes(15));
    }

    @AfterEach
    void tearDown() {
        alerts.findByUserIdAndProductId(owner.getId(), product.getId()).ifPresent(alerts::delete);
        alerts.findByUserIdAndProductId(otherUser.getId(), product.getId()).ifPresent(alerts::delete);
        products.deleteById(product.getId());
        users.deleteAllById(java.util.List.of(owner.getId(), otherUser.getId()));
    }

    @Test
    void rejectsInvalidEmailAndPassword() throws Exception {
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Invalid\",\"email\":\"not-an-email\",\"password\":\"safePass123\"}"))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + owner.getEmail() + "\",\"password\":\"wrong-password\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsExpiredToken() throws Exception {
        String expiredToken = new JwtService(TEST_SECRET, Duration.ofSeconds(-1)).createAccessToken(owner);

        mvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void returnsNotFoundForUnknownProductAndAlertTarget() throws Exception {
        UUID unknown = UUID.randomUUID();
        mvc.perform(get("/api/v1/products/{id}", unknown)).andExpect(status().isNotFound());
        mvc.perform(put("/api/v1/alerts/{id}", unknown)
                        .header("Authorization", bearer(owner)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"targetPrice\":1000}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidPricesAndAnonymousAccess() throws Exception {
        for (String price : new String[]{"0", "-1", "null"}) {
            mvc.perform(put("/api/v1/alerts/{id}", product.getId())
                            .header("Authorization", bearer(owner)).contentType(MediaType.APPLICATION_JSON)
                            .content("{\"targetPrice\":" + price + "}"))
                    .andExpect(status().isBadRequest());
        }
        mvc.perform(get("/api/v1/alerts")).andExpect(status().isForbidden());
    }

    @Test
    void cannotDeleteAnotherUsersAlert() throws Exception {
        mvc.perform(put("/api/v1/alerts/{id}", product.getId())
                        .header("Authorization", bearer(owner)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"targetPrice\":999.90}"))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/v1/alerts/{id}", product.getId())
                        .header("Authorization", bearer(otherUser)))
                .andExpect(status().isNoContent());

        assertThat(alerts.findByUserIdAndProductId(owner.getId(), product.getId())).isPresent();
        assertThat(alerts.findByUserIdAndProductId(otherUser.getId(), product.getId())).isEmpty();
    }

    private User saveUser(String prefix) {
        return users.save(User.builder().name(prefix).email(prefix + "-" + UUID.randomUUID() + "@example.com")
                .passwordHash(passwords.encode("safePass123")).emailVerified(true).status("ACTIVE").build());
    }

    private String bearer(User user) {
        return "Bearer " + jwt.createAccessToken(user);
    }
}
