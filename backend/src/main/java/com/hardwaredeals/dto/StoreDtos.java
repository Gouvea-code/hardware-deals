package com.hardwaredeals.dto;

import java.util.UUID;

public final class StoreDtos {
    private StoreDtos() {}

    public record StoreResponse(UUID id, String name, String slug, String website) {}
}
