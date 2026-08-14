package com.hardwaredeals.repository;

import com.hardwaredeals.entity.OfferClick;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OfferClickRepository extends JpaRepository<OfferClick, UUID> {
    long countByOfferId(UUID offerId);
    void deleteByUserId(UUID userId);
}
