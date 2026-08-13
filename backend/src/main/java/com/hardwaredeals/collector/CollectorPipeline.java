package com.hardwaredeals.collector;

import org.slf4j.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CollectorPipeline {
    private static final Logger log = LoggerFactory.getLogger(CollectorPipeline.class);
    private final CollectedOfferProcessor processor;
    public CollectorPipeline(CollectedOfferProcessor processor) { this.processor = processor; }

    public CollectionResult run(PriceCollector collector) {
        List<CollectedOffer> collected;
        try {
            collected = collector.collect();
        } catch (RuntimeException ex) {
            log.error("Collector source failed: source={}", collector.sourceName(), ex);
            return new CollectionResult(0, 0, 1);
        }
        int persisted = 0;
        int failed = 0;
        for (CollectedOffer offer : collected) {
            try {
                processor.process(offer);
                persisted++;
            } catch (RuntimeException ex) {
                failed++;
                log.error("Collected offer rejected: source={}, externalId={}, reason={}",
                        collector.sourceName(), offer == null ? null : offer.externalId(), ex.getMessage(), ex);
            }
        }
        CollectionResult result = new CollectionResult(collected.size(), persisted, failed);
        log.info("Collector completed: source={}, received={}, persisted={}, failed={}", collector.sourceName(),
                result.received(), result.persisted(), result.failed());
        return result;
    }
}
