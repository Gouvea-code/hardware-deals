package com.hardwaredeals.controller;

import com.hardwaredeals.dto.StoreDtos.StoreResponse;
import com.hardwaredeals.service.StoreService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {
    private final StoreService stores;
    public StoreController(StoreService stores) { this.stores = stores; }

    @GetMapping
    public List<StoreResponse> list() { return stores.findAll(); }

    @GetMapping("/{id}")
    public StoreResponse get(@PathVariable UUID id) { return stores.findById(id); }
}
