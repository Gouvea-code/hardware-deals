package com.hardwaredeals.controller;

import com.hardwaredeals.dto.DealDtos.DealResponse;
import com.hardwaredeals.service.DealQueryService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/deals")
public class DealController {
    private final DealQueryService deals;
    public DealController(DealQueryService deals) { this.deals = deals; }

    @GetMapping
    public List<DealResponse> list(@RequestParam(defaultValue = "score") String sort) {
        return deals.findDeals(sort);
    }

    @GetMapping("/{id}")
    public DealResponse get(@PathVariable UUID id) { return deals.findById(id); }
}
