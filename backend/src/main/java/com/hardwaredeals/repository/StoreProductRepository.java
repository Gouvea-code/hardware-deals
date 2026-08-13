package com.hardwaredeals.repository;

import com.hardwaredeals.entity.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, UUID> {
    Optional<StoreProduct> findByStoreIdAndProductId(UUID storeId, UUID productId);
    List<StoreProduct> findByStoreId(UUID storeId);
    List<StoreProduct> findByProductId(UUID productId);
}
