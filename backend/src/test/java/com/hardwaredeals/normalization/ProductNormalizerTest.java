package com.hardwaredeals.normalization;

import com.hardwaredeals.collector.CollectedOffer;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class ProductNormalizerTest {
    private final ProductNormalizer normalizer = new ProductNormalizer();

    @Test
    void normalizesSpacesCaseAccentsAndSpecialCharacters() {
        assertThat(normalizer.normalizeText("  Pláca--DE   Vídeo!!! ")).isEqualTo("placa de video");
    }

    @Test
    void resolvesEquivalentLongAndCompactGpuNames() {
        String longName = normalizer.normalizeName("ASRock Radeon RX 9070 XT Challenger 16GB", "ASRock");
        String compact = normalizer.normalizeName("asrock RX9070XT Challenger 16 GB", "ASRock");
        assertThat(longName).isEqualTo("rx 9070 xt challenger 16 gb").isEqualTo(compact);
    }

    @Test
    void preservesPerformanceVariantsInsteadOfMergingThem() {
        assertThat(normalizer.normalizeName("RX 9070", "AMD"))
                .isNotEqualTo(normalizer.normalizeName("RX 9070 XT", "AMD"));
        assertThat(normalizer.normalizeName("RTX 5070", "Nvidia"))
                .isNotEqualTo(normalizer.normalizeName("RTX 5070 SUPER", "Nvidia"));
        assertThat(normalizer.normalizeName("RTX 4070", "Nvidia"))
                .isNotEqualTo(normalizer.normalizeName("RTX 4070 Ti", "Nvidia"));
    }

    @Test
    void extractsCapacityMemoryAndVariants() {
        NormalizedProductIdentity identity = normalizer.normalize(offer(
                "GeForce RTX 4070 Ti SUPER GDDR6X 16GB", "Nvidia", "GPU", " SKU- 123 ", "789.123"));
        assertThat(identity.capacities()).containsExactly("16 gb");
        assertThat(identity.memory()).containsExactly("16 gb");
        assertThat(identity.variants()).containsExactly("ti", "super");
        assertThat(identity.sku()).isEqualTo("SKU123");
        assertThat(identity.ean()).isEqualTo("789123");
    }

    @Test
    void keepsDifferentCapacitiesDistinct() {
        assertThat(normalizer.normalizeName("SSD NVMe 1TB", "Acme"))
                .isNotEqualTo(normalizer.normalizeName("SSD NVMe 2TB", "Acme"));
    }

    private CollectedOffer offer(String name, String brand, String category, String sku, String ean) {
        return new CollectedOffer("store", "id", sku, name, brand, name, category, ean,
                "https://example.com", BigDecimal.ONE, BigDecimal.ONE, null, true, null);
    }
}
