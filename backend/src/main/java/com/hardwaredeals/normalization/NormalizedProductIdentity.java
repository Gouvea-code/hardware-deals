package com.hardwaredeals.normalization;

import java.util.List;

public record NormalizedProductIdentity(
        String brand, String model, String normalizedName, String sku, String ean,
        List<String> capacities, List<String> memory, List<String> variants) {
}
