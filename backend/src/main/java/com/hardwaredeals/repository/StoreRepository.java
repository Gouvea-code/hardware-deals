package com.hardwaredeals.repository;

import com.hardwaredeals.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    Optional<Store> findBySlug(String slug);
    Optional<Store> findByIdAndActiveTrue(UUID id);
    List<Store> findAllByActiveTrue(Sort sort);
}
