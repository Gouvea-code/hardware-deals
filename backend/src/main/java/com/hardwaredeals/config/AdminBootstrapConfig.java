package com.hardwaredeals.config;

import com.hardwaredeals.entity.UserRole;
import com.hardwaredeals.repository.UserRepository;
import org.slf4j.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import java.util.*;

@Configuration
public class AdminBootstrapConfig {
 private static final Logger log=LoggerFactory.getLogger(AdminBootstrapConfig.class);
 @Bean @ConditionalOnProperty(name="app.admin.bootstrap-email")
 CommandLineRunner promoteInitialAdmin(UserRepository users,org.springframework.core.env.Environment environment){return args->{
  String email=Objects.requireNonNull(environment.getProperty("app.admin.bootstrap-email")).trim().toLowerCase(Locale.ROOT);
  if(email.isBlank())return;
  users.findByEmail(email).ifPresentOrElse(user->{if(user.getRole()!=UserRole.ADMIN){user.setRole(UserRole.ADMIN);users.save(user);}
   log.info("Initial administrator configured: userId={}",user.getId());},()->log.warn("Admin bootstrap user not found; register and verify the configured account first"));};}
}
