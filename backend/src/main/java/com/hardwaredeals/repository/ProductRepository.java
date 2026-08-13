package com.hardwaredeals.repository;

import com.hardwaredeals.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByEan(String ean);
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
}
