package com.hardwaredeals.controller;

import com.hardwaredeals.dto.ProductDtos.*;
import com.hardwaredeals.service.ProductService;
import com.hardwaredeals.service.PriceHistoryService;
import com.hardwaredeals.dto.PriceHistoryDtos.PriceHistoryResponse;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService products;
    private final PriceHistoryService priceHistory;
    public ProductController(ProductService products, PriceHistoryService priceHistory) {
        this.products = products; this.priceHistory = priceHistory;
    }

    @GetMapping
    public PageResponse<ProductResponse> list(
            @RequestParam(required = false) String category, @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String store, @RequestParam(defaultValue = "name_asc") String sort,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return products.find(null, category, brand, minPrice, maxPrice, store, sort, page, size);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable UUID id) { return products.findById(id); }

    @GetMapping("/{id}/price-history")
    public PriceHistoryResponse history(@PathVariable UUID id) { return priceHistory.get(id); }

    @GetMapping("/search")
    public PageResponse<ProductResponse> search(
            @RequestParam String q, @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand, @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice, @RequestParam(required = false) String store,
            @RequestParam(defaultValue = "name_asc") String sort, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return products.find(q, category, brand, minPrice, maxPrice, store, sort, page, size);
    }
}
