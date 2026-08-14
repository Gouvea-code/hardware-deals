package com.hardwaredeals.controller;

import com.hardwaredeals.dto.DeviceTokenDtos.*; import com.hardwaredeals.service.DeviceTokenService;
import jakarta.validation.Valid; import java.util.UUID; import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/devices")
public class DeviceTokenController {
 private final DeviceTokenService devices; public DeviceTokenController(DeviceTokenService devices){this.devices=devices;}
 @PutMapping public DeviceResponse register(Authentication auth,@Valid @RequestBody RegisterDeviceRequest request){return devices.register(id(auth),request);}
 @DeleteMapping @ResponseStatus(HttpStatus.NO_CONTENT) public void deactivate(Authentication auth,@RequestParam String token){devices.deactivate(id(auth),token);}
 private UUID id(Authentication auth){return UUID.fromString(auth.getName());}
}
