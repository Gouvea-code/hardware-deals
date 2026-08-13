package com.hardwaredeals.service;

import com.hardwaredeals.dto.ProductDtos.*;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.ProductRepository;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService {
    private static final int MAX_PAGE_SIZE = 100;
    private final ProductRepository products;

    public ProductService(ProductRepository products) { this.products = products; }

    public PageResponse<ProductResponse> find(String query, String category, String brand,
                                               BigDecimal minPrice, BigDecimal maxPrice, String store,
                                               String sort, int page, int size) {
        validate(minPrice, maxPrice, page, size);
        if (query != null && !hasText(query)) throw new ApiException(HttpStatus.BAD_REQUEST, "q não pode ser vazio");
        Pageable pageable = PageRequest.of(page, size, resolveSort(sort));
        Page<Product> result = products.findAll(specification(query, category, brand, minPrice, maxPrice, store), pageable);
        List<ProductResponse> content = result.getContent().stream().map(this::toResponse).toList();
        return new PageResponse<>(content, result.getNumber(), result.getSize(), result.getTotalElements(),
                result.getTotalPages(), result.isFirst(), result.isLast());
    }

    public ProductResponse findById(UUID id) {
        Product product = products.findById(id).filter(p -> Boolean.TRUE.equals(p.getActive()))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        return toResponse(product);
    }

    private Specification<Product> specification(String query, String category, String brand,
                                                   BigDecimal minPrice, BigDecimal maxPrice, String store) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));
            if (hasText(query)) {
                String value = '%' + query.trim().toLowerCase(Locale.ROOT) + '%';
                predicates.add(cb.or(cb.like(cb.lower(root.get("name")), value),
                        cb.like(cb.lower(root.get("brand")), value), cb.like(cb.lower(root.get("model")), value)));
            }
            if (hasText(category)) predicates.add(cb.equal(cb.lower(root.get("category")), category.trim().toLowerCase(Locale.ROOT)));
            if (hasText(brand)) predicates.add(cb.equal(cb.lower(root.get("brand")), brand.trim().toLowerCase(Locale.ROOT)));
            if (minPrice != null || maxPrice != null || hasText(store)) {
                Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
                Root<Offer> offer = subquery.from(Offer.class);
                Join<Offer, StoreProduct> storeProduct = offer.join("storeProduct");
                Join<StoreProduct, Store> storeJoin = storeProduct.join("store");
                List<Predicate> offerPredicates = new ArrayList<>();
                offerPredicates.add(cb.equal(storeProduct.get("product"), root));
                offerPredicates.add(cb.isTrue(offer.get("available")));
                offerPredicates.add(cb.isTrue(storeProduct.get("active")));
                offerPredicates.add(cb.isTrue(storeJoin.get("active")));
                if (minPrice != null) offerPredicates.add(cb.greaterThanOrEqualTo(offer.get("price"), minPrice));
                if (maxPrice != null) offerPredicates.add(cb.lessThanOrEqualTo(offer.get("price"), maxPrice));
                if (hasText(store)) offerPredicates.add(cb.equal(cb.lower(storeJoin.get("slug")), store.trim().toLowerCase(Locale.ROOT)));
                subquery.select(cb.literal(1)).where(offerPredicates.toArray(Predicate[]::new));
                predicates.add(cb.exists(subquery));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Sort resolveSort(String value) {
        return switch (value == null ? "name_asc" : value.toLowerCase(Locale.ROOT)) {
            case "name_asc" -> Sort.by("name").ascending();
            case "name_desc" -> Sort.by("name").descending();
            case "newest" -> Sort.by("createdAt").descending();
            case "brand_asc" -> Sort.by("brand").ascending().and(Sort.by("name").ascending());
            default -> throw new ApiException(HttpStatus.BAD_REQUEST, "Ordenação inválida");
        };
    }

    private void validate(BigDecimal min, BigDecimal max, int page, int size) {
        if (page < 0) throw new ApiException(HttpStatus.BAD_REQUEST, "page deve ser maior ou igual a zero");
        if (size < 1 || size > MAX_PAGE_SIZE) throw new ApiException(HttpStatus.BAD_REQUEST, "size deve estar entre 1 e 100");
        if (min != null && min.signum() < 0 || max != null && max.signum() < 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Preço não pode ser negativo");
        if (min != null && max != null && min.compareTo(max) > 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "minPrice não pode ser maior que maxPrice");
    }

    private boolean hasText(String value) { return value != null && !value.isBlank(); }
    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getBrand(), p.getModel(), p.getCategory(), p.getEan(), p.getImageUrl());
    }
}
