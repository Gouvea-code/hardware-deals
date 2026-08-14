package com.hardwaredeals.controller;
import com.hardwaredeals.entity.*;import com.hardwaredeals.repository.*;import java.util.*;
import org.junit.jupiter.api.*;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;import org.springframework.http.MediaType;import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") class DeviceTokenControllerTest{
 @Autowired MockMvc mvc;@Autowired DeviceTokenRepository devices;@Autowired UserRepository users;private User user;
 @BeforeEach void setup(){devices.deleteAll();user=users.save(User.builder().name("Push").email("push-"+UUID.randomUUID()+"@example.com").passwordHash("h").emailVerified(true).build());}
 @Test void registersRefreshesAndDeactivatesToken()throws Exception{mvc.perform(put("/api/v1/devices").with(authentication(auth())).contentType(MediaType.APPLICATION_JSON)
  .content("{\"token\":\"fcm-token\",\"platform\":\"android\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.active").value(true));
  mvc.perform(delete("/api/v1/devices").param("token","fcm-token").with(authentication(auth()))).andExpect(status().isNoContent());
  Assertions.assertFalse(devices.findByToken("fcm-token").orElseThrow().getActive());}
 @Test void validatesPlatformAndProtectsEndpoint()throws Exception{mvc.perform(put("/api/v1/devices").contentType(MediaType.APPLICATION_JSON).content("{\"token\":\"x\",\"platform\":\"web\"}"))
  .andExpect(status().isForbidden());mvc.perform(put("/api/v1/devices").with(authentication(auth())).contentType(MediaType.APPLICATION_JSON)
  .content("{\"token\":\"x\",\"platform\":\"web\"}")).andExpect(status().isBadRequest());}
 private UsernamePasswordAuthenticationToken auth(){return new UsernamePasswordAuthenticationToken(user.getId().toString(),null,List.of());}
}
