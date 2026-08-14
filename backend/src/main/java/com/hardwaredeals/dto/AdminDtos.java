package com.hardwaredeals.dto;

import com.hardwaredeals.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public final class AdminDtos {
 private AdminDtos() {}
 public record DashboardResponse(long users,long activeUsers,long products,long activeProducts,long stores,
  long activeStores,long offers,long availableOffers,long alerts,long notifications,long clicks,long analyticsEvents){}
 public record UserAdminResponse(UUID id,String name,String email,String status,UserRole role,boolean emailVerified,LocalDateTime createdAt){}
 public record UpdateUserRequest(@NotNull String status,@NotNull UserRole role){}
 public record ActiveRequest(@NotNull Boolean active){}
 public record ProductAdminResponse(UUID id,String name,String brand,String model,String category,boolean active){}
 public record StoreAdminResponse(UUID id,String name,String slug,String website,boolean active){}
 public record OfferAdminResponse(UUID id,UUID productId,String productName,UUID storeId,String storeName,
  BigDecimal price,boolean available,LocalDateTime collectedAt){}
 public record CollectorStatusResponse(boolean enabled,boolean feedConfigured,String cron){}
 public record ReportResponse(long offerClicks,long analyticsEvents,long notifications,long activeAlerts){}
 public record AuditResponse(UUID id,UUID adminId,String action,String targetType,UUID targetId,LocalDateTime createdAt){}
}
