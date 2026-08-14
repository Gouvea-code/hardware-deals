package com.hardwaredeals.service;

import com.hardwaredeals.dto.AnalyticsDtos.*;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.UUID;

@Service
@Transactional
public class AnalyticsService {
    private final AnalyticsEventRepository events;
    private final UserRepository users;
    private final ProductRepository products;
    private final NotificationRepository notifications;
    private final Duration retention;

    public AnalyticsService(AnalyticsEventRepository events, UserRepository users, ProductRepository products,
                            NotificationRepository notifications,
                            @Value("${app.analytics.retention:P90D}") Duration retention) {
        this.events = events; this.users = users; this.products = products;
        this.notifications = notifications; this.retention = retention;
    }

    public AnalyticsEventResponse record(AnalyticsEventRequest request, UUID userId) {
        if (request.eventType() == AnalyticsEventType.OFFER_CLICK)
            throw new ApiException(HttpStatus.BAD_REQUEST, "offer_click é registrado pelo redirecionamento");
        User user = findUser(userId);
        Product product = null;
        Notification notification = null;
        switch (request.eventType()) {
            case APP_OPEN, SEARCH -> requireNoContext(request);
            case PRODUCT_VIEW -> product = requireProduct(request.productId());
            case FAVORITE, ALERT_CREATED -> {
                if (user == null) throw new ApiException(HttpStatus.UNAUTHORIZED, "Autenticação obrigatória");
                product = requireProduct(request.productId());
            }
            case NOTIFICATION_OPEN -> {
                if (user == null) throw new ApiException(HttpStatus.UNAUTHORIZED, "Autenticação obrigatória");
                if (request.productId() != null || request.notificationId() == null)
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Contexto inválido para notification_open");
                notification = notifications.findById(request.notificationId())
                        .filter(value -> value.getUser().getId().equals(userId))
                        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));
            }
            default -> throw new ApiException(HttpStatus.BAD_REQUEST, "Evento inválido");
        }
        AnalyticsEvent saved = events.save(AnalyticsEvent.builder().eventType(request.eventType())
                .user(user).product(product).notification(notification).build());
        return new AnalyticsEventResponse(saved.getId(), saved.getEventType(), saved.getOccurredAt());
    }

    public void recordOfferClick(User user, Offer offer) {
        events.save(AnalyticsEvent.builder().eventType(AnalyticsEventType.OFFER_CLICK).user(user)
                .product(offer.getStoreProduct().getProduct()).offer(offer).build());
    }

    @Scheduled(cron = "${app.analytics.cleanup-cron:0 30 3 * * *}")
    public long deleteExpired() { return events.deleteByOccurredAtBefore(LocalDateTime.now().minus(retention)); }

    private User findUser(UUID userId) {
        return userId == null ? null : users.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Sessão inválida"));
    }
    private Product requireProduct(UUID id) {
        if (id == null) throw new ApiException(HttpStatus.BAD_REQUEST, "productId é obrigatório");
        return products.findById(id).filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
    private void requireNoContext(AnalyticsEventRequest request) {
        if (request.productId() != null || request.notificationId() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "O evento não aceita contexto adicional");
    }
}
