package com.hardwaredeals.collector;

import java.util.List;

public interface PriceCollector {
    String sourceName();
    List<CollectedOffer> collect();
}
