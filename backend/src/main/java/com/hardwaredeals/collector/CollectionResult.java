package com.hardwaredeals.collector;

public record CollectionResult(int received, int persisted, int failed) {}
