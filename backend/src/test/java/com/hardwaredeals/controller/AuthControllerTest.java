package com.hardwaredeals.controller;

import com.hardwaredeals.entity.User;
import com.hardwaredeals.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.concurrent.atomic.AtomicReference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired PasswordEncoder passwordEncoder;
    @MockBean JavaMailSender mailSender;

    @Test
    void completeAuthenticationLifecycle() throws Exception {
        AtomicReference<SimpleMailMessage> sent = captureEmail();
        mvc.perform(post("/api/v1/auth/register").contentType("application/json")
                        .content("{\"name\":\"Ada\",\"email\":\"ADA@example.com\",\"password\":\"safePass123\"}"))
                .andExpect(status().isCreated());

        User user = users.findByEmail("ada@example.com").orElseThrow();
        assertThat(user.getPasswordHash()).startsWith("$2").doesNotContain("safePass123");
        mvc.perform(post("/api/v1/auth/login").contentType("application/json")
                        .content("{\"email\":\"ada@example.com\",\"password\":\"safePass123\"}"))
                .andExpect(status().isForbidden());

        String verification = tokenFrom(sent.get().getText());
        mvc.perform(post("/api/v1/auth/verify-email").contentType("application/json")
                        .content("{\"token\":\"" + verification + "\"}"))
                .andExpect(status().isOk());

        String login = mvc.perform(post("/api/v1/auth/login").contentType("application/json")
                        .content("{\"email\":\"ada@example.com\",\"password\":\"safePass123\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn().getResponse().getContentAsString();
        String access = JsonPath.read(login, "$.accessToken");
        String refresh = JsonPath.read(login, "$.refreshToken");

        mvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer " + access))
                .andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(user.getId().toString()));
        mvc.perform(get("/api/v1/auth/me")).andExpect(status().isForbidden());

        String refreshed = mvc.perform(post("/api/v1/auth/refresh").contentType("application/json")
                        .content("{\"refreshToken\":\"" + refresh + "\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String rotatedRefresh = JsonPath.read(refreshed, "$.refreshToken");
        mvc.perform(post("/api/v1/auth/refresh").contentType("application/json")
                        .content("{\"refreshToken\":\"" + refresh + "\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/v1/auth/logout").contentType("application/json")
                        .content("{\"refreshToken\":\"" + rotatedRefresh + "\"}"))
                .andExpect(status().isOk());
        mvc.perform(post("/api/v1/auth/refresh").contentType("application/json")
                        .content("{\"refreshToken\":\"" + rotatedRefresh + "\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void forgotAndResetPasswordDoesNotRevealUnknownEmail() throws Exception {
        AtomicReference<SimpleMailMessage> sent = captureEmail();
        User user = users.save(User.builder().name("Grace").email("grace@example.com")
                .passwordHash(passwordEncoder.encode("oldSafePass123")).status("ACTIVE").emailVerified(true).build());
        String login = mvc.perform(post("/api/v1/auth/login").contentType("application/json")
                        .content("{\"email\":\"grace@example.com\",\"password\":\"oldSafePass123\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String refresh = JsonPath.read(login, "$.refreshToken");
        mvc.perform(post("/api/v1/auth/forgot-password").contentType("application/json")
                        .content("{\"email\":\"unknown@example.com\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.message").exists());
        mvc.perform(post("/api/v1/auth/forgot-password").contentType("application/json")
                        .content("{\"email\":\"" + user.getEmail() + "\"}"))
                .andExpect(status().isOk());
        String reset = tokenFrom(sent.get().getText());
        mvc.perform(post("/api/v1/auth/reset-password").contentType("application/json")
                        .content("{\"token\":\"" + reset + "\",\"newPassword\":\"newSafePass456\"}"))
                .andExpect(status().isOk());
        mvc.perform(post("/api/v1/auth/refresh").contentType("application/json")
                        .content("{\"refreshToken\":\"" + refresh + "\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/v1/auth/reset-password").contentType("application/json")
                        .content("{\"token\":\"" + reset + "\",\"newPassword\":\"anotherPass789\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsInvalidRegistrationAndDuplicateEmail() throws Exception {
        captureEmail();
        mvc.perform(post("/api/v1/auth/register").contentType("application/json")
                        .content("{\"name\":\"A\",\"email\":\"invalid\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest());
        String body = "{\"name\":\"Linus\",\"email\":\"linus@example.com\",\"password\":\"safePass123\"}";
        mvc.perform(post("/api/v1/auth/register").contentType("application/json").content(body))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/v1/auth/register").contentType("application/json").content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void deletesAuthenticatedAccountAfterPasswordConfirmation() throws Exception {
        User user = users.save(User.builder().name("Delete Me").email("delete@example.com")
                .passwordHash(passwordEncoder.encode("safePass123")).status("ACTIVE").emailVerified(true).build());
        String login = mvc.perform(post("/api/v1/auth/login").contentType("application/json")
                        .content("{\"email\":\"delete@example.com\",\"password\":\"safePass123\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String access = JsonPath.read(login, "$.accessToken");

        mvc.perform(delete("/api/v1/auth/me").header("Authorization", "Bearer " + access)
                        .contentType("application/json").content("{\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/v1/auth/me").header("Authorization", "Bearer " + access)
                        .contentType("application/json").content("{\"password\":\"safePass123\"}"))
                .andExpect(status().isOk());
        assertThat(users.findById(user.getId())).isEmpty();
    }

    private AtomicReference<SimpleMailMessage> captureEmail() {
        AtomicReference<SimpleMailMessage> sent = new AtomicReference<>();
        doAnswer(invocation -> { sent.set(invocation.getArgument(0)); return null; })
                .when(mailSender).send(any(SimpleMailMessage.class));
        return sent;
    }

    private String tokenFrom(String text) { return text.substring(text.indexOf("token=") + 6); }
}
