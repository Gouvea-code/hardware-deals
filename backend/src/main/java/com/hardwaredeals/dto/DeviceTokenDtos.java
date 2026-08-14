package com.hardwaredeals.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

public final class DeviceTokenDtos {
    private DeviceTokenDtos() {}
    public record RegisterDeviceRequest(@NotBlank @Size(max = 4096) String token,
            @NotBlank @Pattern(regexp = "android|ios") String platform) {}
    public record DeviceResponse(UUID id, String platform, boolean active, LocalDateTime updatedAt) {}
}
