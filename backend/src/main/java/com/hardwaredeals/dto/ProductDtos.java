package com.hardwaredeals.dto;

import java.util.List;
import java.util.UUID;

public final class ProductDtos {
    private ProductDtos() {}

    public record ProductResponse(UUID id, String name, String brand, String model,
                                  String category, String ean, String imageUrl) {}

    public record PageResponse<T>(List<T> content, int page, int size, long totalElements,
                                  int totalPages, boolean first, boolean last) {}
}
