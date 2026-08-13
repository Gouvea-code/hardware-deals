package com.hardwaredeals.normalization;

import com.hardwaredeals.entity.Product;
import com.hardwaredeals.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductMatchingService {
    private final ProductRepository products;
    public ProductMatchingService(ProductRepository products) { this.products = products; }

    public Optional<Product> findMatch(NormalizedProductIdentity identity) {
        Optional<Product> byEan = products.findByEan(identity.ean());
        if (byEan.isPresent()) return byEan;
        return products.findByBrandIgnoreCaseAndNormalizedName(identity.brand(), identity.normalizedName());
    }
}
