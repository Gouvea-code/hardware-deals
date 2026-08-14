package com.hardwaredeals.controller;

import com.hardwaredeals.dto.OfferClickDtos.OfferRedirectResponse;
import com.hardwaredeals.service.OfferRedirectService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferRedirectController {
    private final OfferRedirectService redirects;
    public OfferRedirectController(OfferRedirectService redirects) { this.redirects = redirects; }

    @PostMapping("/{offerId}/click")
    public OfferRedirectResponse register(@PathVariable UUID offerId, Authentication authentication) {
        UUID userId = authentication == null ? null : UUID.fromString(authentication.getName());
        return redirects.register(offerId, userId);
    }
}
