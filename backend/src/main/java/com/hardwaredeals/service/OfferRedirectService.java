package com.hardwaredeals.service;

import com.hardwaredeals.dto.OfferClickDtos.OfferRedirectResponse;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class OfferRedirectService {
    private final OfferRepository offers;
    private final OfferClickRepository clicks;
    private final UserRepository users;

    public OfferRedirectService(OfferRepository offers, OfferClickRepository clicks, UserRepository users) {
        this.offers = offers; this.clicks = clicks; this.users = users;
    }

    public OfferRedirectResponse register(UUID offerId, UUID userId) {
        Offer offer = offers.findById(offerId)
                .filter(this::isAvailable)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Oferta não encontrada"));
        StoreProduct storeProduct = offer.getStoreProduct();
        validateDestination(storeProduct.getUrl(), storeProduct.getStore().getWebsite());
        User user = userId == null ? null : users.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Sessão inválida"));
        LocalDateTime now = LocalDateTime.now();
        OfferClick click = clicks.save(OfferClick.builder().user(user).offer(offer)
                .product(storeProduct.getProduct()).store(storeProduct.getStore()).clickedAt(now).build());
        return new OfferRedirectResponse(click.getId(), storeProduct.getUrl(), now);
    }

    private boolean isAvailable(Offer offer) {
        StoreProduct sp = offer.getStoreProduct();
        return Boolean.TRUE.equals(offer.getAvailable()) && Boolean.TRUE.equals(sp.getActive())
                && Boolean.TRUE.equals(sp.getProduct().getActive()) && Boolean.TRUE.equals(sp.getStore().getActive());
    }

    private void validateDestination(String rawDestination, String rawStoreWebsite) {
        try {
            URI destination = URI.create(rawDestination);
            URI storeWebsite = URI.create(rawStoreWebsite);
            String destinationHost = destination.getHost();
            String storeHost = storeWebsite.getHost();
            if (!"https".equalsIgnoreCase(destination.getScheme()) || destinationHost == null || storeHost == null
                    || !(destinationHost.equalsIgnoreCase(storeHost)
                    || destinationHost.toLowerCase().endsWith("." + storeHost.toLowerCase()))) {
                throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Destino da oferta não é confiável");
            }
        } catch (ApiException ex) {
            throw ex;
        } catch (IllegalArgumentException ex) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Destino da oferta é inválido");
        }
    }
}
