package com.hardwaredeals.controller;

import com.hardwaredeals.dto.AdminDtos.*;
import com.hardwaredeals.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/v1/admin")
public class AdminController {
 private final AdminService admin;public AdminController(AdminService admin){this.admin=admin;}
 @GetMapping("/dashboard") public DashboardResponse dashboard(){return admin.dashboard();}
 @GetMapping("/users") public List<UserAdminResponse> users(){return admin.users();}
 @PatchMapping("/users/{id}") public UserAdminResponse updateUser(Authentication auth,@PathVariable UUID id,@Valid @RequestBody UpdateUserRequest request){return admin.updateUser(actor(auth),id,request);}
 @GetMapping("/products") public List<ProductAdminResponse> products(){return admin.products();}
 @PatchMapping("/products/{id}/active") public ProductAdminResponse updateProduct(Authentication auth,@PathVariable UUID id,@Valid @RequestBody ActiveRequest request){return admin.updateProduct(actor(auth),id,request);}
 @GetMapping("/stores") public List<StoreAdminResponse> stores(){return admin.stores();}
 @PatchMapping("/stores/{id}/active") public StoreAdminResponse updateStore(Authentication auth,@PathVariable UUID id,@Valid @RequestBody ActiveRequest request){return admin.updateStore(actor(auth),id,request);}
 @GetMapping("/offers") public List<OfferAdminResponse> offers(){return admin.offers();}
 @GetMapping("/collectors") public CollectorStatusResponse collectors(){return admin.collector();}
 @GetMapping("/reports") public ReportResponse reports(){return admin.reports();}
 @GetMapping("/audit") public List<AuditResponse> audit(){return admin.audit();}
 private UUID actor(Authentication auth){return UUID.fromString(auth.getName());}
}
