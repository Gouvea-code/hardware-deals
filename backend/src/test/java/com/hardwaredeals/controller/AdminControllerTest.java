package com.hardwaredeals.controller;

import com.hardwaredeals.entity.*;
import com.hardwaredeals.repository.*;
import com.hardwaredeals.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class AdminControllerTest {
 @Autowired MockMvc mvc;@Autowired UserRepository users;@Autowired PasswordEncoder passwords;@Autowired JwtService jwt;
 @Autowired AdminAuditRepository audit;

 @Test void protectsAdministrationAndAuditsChanges()throws Exception{
  User regular=save("regular-admin-test@example.com",UserRole.USER);User admin=save("admin-test@example.com",UserRole.ADMIN);
  mvc.perform(get("/api/v1/admin/dashboard").header("Authorization","Bearer "+jwt.createAccessToken(regular)))
   .andExpect(status().isForbidden());
  mvc.perform(get("/api/v1/admin/dashboard").header("Authorization","Bearer "+jwt.createAccessToken(admin)))
   .andExpect(status().isOk()).andExpect(jsonPath("$.users").isNumber()).andExpect(jsonPath("$.analyticsEvents").isNumber());
  mvc.perform(patch("/api/v1/admin/users/{id}",regular.getId()).header("Authorization","Bearer "+jwt.createAccessToken(admin))
   .contentType("application/json").content("{\"status\":\"INACTIVE\",\"role\":\"USER\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("INACTIVE"));
  assertThat(audit.findTop100ByOrderByCreatedAtDesc()).anyMatch(value->value.getTargetId().equals(regular.getId()));
 }

 @Test void administratorCannotRemoveOwnAccess()throws Exception{
  User admin=save("self-admin-test@example.com",UserRole.ADMIN);
  mvc.perform(patch("/api/v1/admin/users/{id}",admin.getId()).header("Authorization","Bearer "+jwt.createAccessToken(admin))
   .contentType("application/json").content("{\"status\":\"INACTIVE\",\"role\":\"USER\"}"))
   .andExpect(status().isConflict());
 }
 private User save(String email,UserRole role){return users.save(User.builder().name("Admin Test").email(email)
  .passwordHash(passwords.encode("safePass123")).status("ACTIVE").emailVerified(true).role(role).build());}
}
