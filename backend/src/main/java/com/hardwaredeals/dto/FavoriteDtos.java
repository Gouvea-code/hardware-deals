package com.hardwaredeals.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class FavoriteDtos {
    private FavoriteDtos() {}
    public record FavoriteResponse(UUID id, UUID productId, String productName, String brand,
                                   String category, String imageUrl, LocalDateTime createdAt) {}
}
