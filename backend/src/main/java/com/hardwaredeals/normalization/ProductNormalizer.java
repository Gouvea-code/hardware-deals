package com.hardwaredeals.normalization;

import com.hardwaredeals.collector.CollectedOffer;
import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.*;

@Service
public class ProductNormalizer {
    private static final Set<String> GENERIC_GPU_WORDS = Set.of("radeon", "geforce");
    private static final Set<String> VARIANTS = Set.of("xt", "super", "ti");
    private static final Pattern CAPACITY = Pattern.compile("\\b\\d+(?:[.,]\\d+)?\\s*(?:kb|mb|gb|tb)\\b");
    private static final Pattern MEMORY_CONTEXT = Pattern.compile("\\b(?:vram|ram|gddr\\d*|ddr\\d*)\\b");

    public NormalizedProductIdentity normalize(CollectedOffer offer) {
        String brand = normalizeText(offer.brand());
        String model = normalizeText(offer.model());
        String name = normalizeName(offer.productName(), offer.brand());
        List<String> capacities = matches(CAPACITY, name);
        List<String> memory = isMemoryProduct(offer.category(), name) ? capacities : List.of();
        List<String> variants = tokens(name).stream().filter(VARIANTS::contains).distinct().toList();
        return new NormalizedProductIdentity(brand, model, name, normalizeSku(offer.sku()),
                normalizeEan(offer.ean()), capacities, memory, variants);
    }

    public String normalizeName(String rawName, String rawBrand) {
        String name = normalizeText(rawName);
        Set<String> brandTokens = new HashSet<>(tokens(normalizeText(rawBrand)));
        return tokens(name).stream()
                .filter(token -> !brandTokens.contains(token))
                .filter(token -> !GENERIC_GPU_WORDS.contains(token))
                .reduce((left, right) -> left + " " + right).orElse("");
    }

    public String normalizeSku(String sku) {
        return normalizeText(sku).replace(" ", "").toUpperCase(Locale.ROOT);
    }

    public String normalizeEan(String ean) {
        return ean == null ? "" : ean.replaceAll("\\D", "");
    }

    public String normalizeText(String value) {
        if (value == null) return "";
        String ascii = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return ascii.toLowerCase(Locale.ROOT)
                .replaceAll("(?<=\\p{L})(?=\\d)|(?<=\\d)(?=\\p{L})", " ")
                .replaceAll("[^a-z0-9]+", " ").trim().replaceAll("\\s+", " ");
    }

    private List<String> tokens(String value) { return value.isBlank() ? List.of() : List.of(value.split(" ")); }
    private List<String> matches(Pattern pattern, String value) {
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) result.add(matcher.group().replace(',', '.').replaceAll("\\s+", " "));
        return result.stream().distinct().toList();
    }
    private boolean isMemoryProduct(String category, String name) {
        String normalizedCategory = normalizeText(category);
        return Set.of("gpu", "ram").contains(normalizedCategory) || MEMORY_CONTEXT.matcher(name).find();
    }
}
