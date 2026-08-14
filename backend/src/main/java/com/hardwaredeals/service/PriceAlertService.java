package com.hardwaredeals.service;

import com.hardwaredeals.dto.PriceAlertDtos.*;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriceAlertService {
    private final PriceAlertRepository alerts; private final ProductRepository products; private final UserRepository users;
    public PriceAlertService(PriceAlertRepository alerts, ProductRepository products, UserRepository users) {
        this.alerts = alerts; this.products = products; this.users = users;
    }
    @Transactional(readOnly = true)
    public List<AlertResponse> list(UUID userId) {
        return alerts.findByUserId(userId).stream().sorted(Comparator.comparing(PriceAlert::getUpdatedAt).reversed())
                .map(this::response).toList();
    }
    @Transactional
    public AlertResponse save(UUID userId, UUID productId, SaveAlertRequest request) {
        PriceAlert alert = alerts.findByUserIdAndProductId(userId, productId).orElseGet(() -> {
            User user = users.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
            Product product = products.findById(productId).filter(p -> Boolean.TRUE.equals(p.getActive()))
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
            return PriceAlert.builder().user(user).product(product).build();
        });
        alert.setTargetPrice(request.targetPrice()); alert.setActive(true);
        return response(alerts.save(alert));
    }
    @Transactional public void remove(UUID userId, UUID productId) {
        alerts.findByUserIdAndProductId(userId, productId).ifPresent(alerts::delete);
    }
    private AlertResponse response(PriceAlert alert) {
        Product p = alert.getProduct();
        return new AlertResponse(alert.getId(), p.getId(), p.getName(), p.getBrand(), p.getImageUrl(),
                alert.getTargetPrice(), Boolean.TRUE.equals(alert.getActive()), alert.getCreatedAt(), alert.getUpdatedAt());
    }
}
