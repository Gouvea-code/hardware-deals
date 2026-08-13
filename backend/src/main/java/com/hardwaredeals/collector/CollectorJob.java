package com.hardwaredeals.collector;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app.collector", name = "enabled", havingValue = "true")
public class CollectorJob {
    private final List<PriceCollector> collectors;
    private final CollectorPipeline pipeline;
    public CollectorJob(List<PriceCollector> collectors, CollectorPipeline pipeline) {
        this.collectors = collectors; this.pipeline = pipeline;
    }

    @Scheduled(cron = "${app.collector.cron:0 0 * * * *}")
    public void collect() { collectors.forEach(pipeline::run); }
}
