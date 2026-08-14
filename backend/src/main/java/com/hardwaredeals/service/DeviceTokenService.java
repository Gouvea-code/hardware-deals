package com.hardwaredeals.service;

import com.hardwaredeals.dto.DeviceTokenDtos.*; import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException; import com.hardwaredeals.repository.*; import java.util.UUID;
import org.springframework.http.HttpStatus; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceTokenService {
 private final DeviceTokenRepository devices; private final UserRepository users;
 public DeviceTokenService(DeviceTokenRepository devices,UserRepository users){this.devices=devices;this.users=users;}
 @Transactional public DeviceResponse register(UUID userId,RegisterDeviceRequest request){
  User user=users.findById(userId).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Usuário não encontrado"));
  DeviceToken device=devices.findByToken(request.token()).orElseGet(()->DeviceToken.builder().token(request.token()).build());
  device.setUser(user);device.setPlatform(request.platform());device.setActive(true);return response(devices.save(device));
 }
 @Transactional public void deactivate(UUID userId,String token){devices.findByToken(token)
  .filter(device->device.getUser().getId().equals(userId)).ifPresent(device->{device.setActive(false);devices.save(device);});}
 private DeviceResponse response(DeviceToken d){return new DeviceResponse(d.getId(),d.getPlatform(),Boolean.TRUE.equals(d.getActive()),d.getUpdatedAt());}
}
