package com.hardwaredeals.repository;

import com.hardwaredeals.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {
    List<PriceHistory> findByProductIdOrderByCollectedAtDesc(UUID productId);
    List<PriceHistory> findByStoreIdOrderByCollectedAtDesc(UUID storeId);
    List<PriceHistory> findByProductIdAndStoreIdOrderByCollectedAtDesc(UUID productId, UUID storeId);
}
