package com.hardwaredeals.controller;

import com.hardwaredeals.entity.*; import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.*; import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles; import org.springframework.test.web.servlet.MockMvc; import java.util.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class PriceAlertControllerTest {
    @Autowired MockMvc mvc; @Autowired PriceAlertRepository alerts; @Autowired ProductRepository products; @Autowired UserRepository users;
    private User user; private Product product;
    @BeforeEach void setup() {
        alerts.deleteAll();
        user=users.save(User.builder().name("Alert").email("alert-"+UUID.randomUUID()+"@example.com").passwordHash("h").emailVerified(true).build());
        product=products.save(Product.builder().name("RX 9070 XT").brand("AMD").model("9070").category("GPU")
                .ean(String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits()))).normalizedName("rx 9070 xt").active(true).build());
    }
    @Test void createsUpdatesListsAndRemovesAlert() throws Exception {
        mvc.perform(put("/api/v1/alerts/{id}",product.getId()).with(authentication(auth())).contentType(MediaType.APPLICATION_JSON)
                .content("{\"targetPrice\":3700}")).andExpect(status().isOk()).andExpect(jsonPath("$.targetPrice").value(3700));
        mvc.perform(put("/api/v1/alerts/{id}",product.getId()).with(authentication(auth())).contentType(MediaType.APPLICATION_JSON)
                .content("{\"targetPrice\":3500}")).andExpect(status().isOk()).andExpect(jsonPath("$.targetPrice").value(3500));
        mvc.perform(get("/api/v1/alerts").with(authentication(auth()))).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(delete("/api/v1/alerts/{id}",product.getId()).with(authentication(auth()))).andExpect(status().isNoContent());
    }
    @Test void validatesTargetAndAuthentication() throws Exception {
        mvc.perform(get("/api/v1/alerts")).andExpect(status().isForbidden());
        mvc.perform(put("/api/v1/alerts/{id}",product.getId()).with(authentication(auth())).contentType(MediaType.APPLICATION_JSON)
                .content("{\"targetPrice\":0}")).andExpect(status().isBadRequest());
    }
    private UsernamePasswordAuthenticationToken auth(){return new UsernamePasswordAuthenticationToken(user.getId().toString(),null,List.of());}
}
