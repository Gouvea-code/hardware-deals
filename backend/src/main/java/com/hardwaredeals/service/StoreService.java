package com.hardwaredeals.service;

import com.hardwaredeals.dto.StoreDtos.StoreResponse;
import com.hardwaredeals.entity.Store;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.StoreRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository stores;
    public StoreService(StoreRepository stores) { this.stores = stores; }

    public List<StoreResponse> findAll() {
        return stores.findAllByActiveTrue(Sort.by(Sort.Direction.ASC, "name")).stream().map(this::toResponse).toList();
    }

    public StoreResponse findById(UUID id) {
        return stores.findByIdAndActiveTrue(id).map(this::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Loja não encontrada"));
    }

    private StoreResponse toResponse(Store store) {
        return new StoreResponse(store.getId(), store.getName(), store.getSlug(), store.getWebsite());
    }
}
