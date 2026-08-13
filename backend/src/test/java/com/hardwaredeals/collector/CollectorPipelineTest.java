package com.hardwaredeals.collector;

import com.hardwaredeals.entity.Store;
import com.hardwaredeals.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CollectorPipelineTest {
    @Autowired CollectorPipeline pipeline;
    @Autowired StoreRepository stores;
    @Autowired StoreProductRepository storeProducts;
    @Autowired ProductRepository products;
    @Autowired OfferRepository offers;
    @Autowired PriceHistoryRepository history;

    @BeforeEach
    void setUp() {
        offers.deleteAll(); history.deleteAll(); storeProducts.deleteAll(); products.deleteAll(); stores.deleteAll();
        stores.save(Store.builder().name("Source Store").slug("source-store")
                .website("https://store.example").active(true).build());
    }

    @Test
    void persistsValidOffersAndHistoryWhileIsolatingInvalidItems() {
        PriceCollector collector = collector(List.of(valid("one", "111", "100.00"),
                valid("broken", "222", "-1.00"), valid("three", "333", "300.00")));
        CollectionResult result = pipeline.run(collector);

        assertThat(result).isEqualTo(new CollectionResult(3, 2, 1));
        assertThat(products.count()).isEqualTo(2);
        assertThat(storeProducts.count()).isEqualTo(2);
        assertThat(offers.count()).isEqualTo(2);
        assertThat(history.count()).isEqualTo(2);
        assertThat(products.findByEan("111").orElseThrow().getNormalizedName()).isEqualTo("gpu one");
    }

    @Test
    void recordsSourceFailureWithoutThrowingFromJobPipeline() {
        PriceCollector failing = new PriceCollector() {
            public String sourceName() { return "failing-source"; }
            public List<CollectedOffer> collect() { throw new IllegalStateException("source unavailable"); }
        };
        assertThat(pipeline.run(failing)).isEqualTo(new CollectionResult(0, 0, 1));
        assertThat(offers.count()).isZero();
    }

    private PriceCollector collector(List<CollectedOffer> values) {
        return new PriceCollector() {
            public String sourceName() { return "test-source"; }
            public List<CollectedOffer> collect() { return values; }
        };
    }

    private CollectedOffer valid(String externalId, String ean, String price) {
        return new CollectedOffer("source-store", externalId, "sku-" + externalId, "  GPU " + externalId + "  ",
                "Brand", "Model", "GPU", ean, "https://store.example/" + externalId,
                new BigDecimal(price), null, null, true, null);
    }
}
