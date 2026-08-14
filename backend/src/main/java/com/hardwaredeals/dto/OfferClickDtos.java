package com.hardwaredeals.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OfferClickDtos {
    private OfferClickDtos() {}
    public record OfferRedirectResponse(UUID clickId, String redirectUrl, LocalDateTime clickedAt) {}
}
