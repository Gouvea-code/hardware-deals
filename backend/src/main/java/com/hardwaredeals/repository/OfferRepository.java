package com.hardwaredeals.repository;

import com.hardwaredeals.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findByStoreProductId(UUID storeProductId);
    List<Offer> findAllByOrderByCollectedAtDesc();
    List<Offer> findByStoreProductProductIdOrderByCollectedAtDesc(UUID productId);
}
