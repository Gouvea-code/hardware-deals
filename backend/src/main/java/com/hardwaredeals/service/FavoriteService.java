package com.hardwaredeals.service;

import com.hardwaredeals.dto.FavoriteDtos.FavoriteResponse;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {
    private final FavoriteRepository favorites;
    private final ProductRepository products;
    private final UserRepository users;
    public FavoriteService(FavoriteRepository favorites, ProductRepository products, UserRepository users) {
        this.favorites = favorites; this.products = products; this.users = users;
    }
    @Transactional(readOnly = true)
    public List<FavoriteResponse> list(UUID userId) {
        return favorites.findByUserId(userId).stream().sorted(Comparator.comparing(Favorite::getCreatedAt).reversed())
                .map(this::toResponse).toList();
    }
    @Transactional
    public FavoriteResponse add(UUID userId, UUID productId) {
        return favorites.findByUserIdAndProductId(userId, productId).map(this::toResponse).orElseGet(() -> {
            User user = users.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
            Product product = products.findById(productId).filter(p -> Boolean.TRUE.equals(p.getActive()))
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
            return toResponse(favorites.save(Favorite.builder().user(user).product(product).build()));
        });
    }
    @Transactional
    public void remove(UUID userId, UUID productId) {
        favorites.findByUserIdAndProductId(userId, productId).ifPresent(favorites::delete);
    }
    private FavoriteResponse toResponse(Favorite favorite) {
        Product product = favorite.getProduct();
        return new FavoriteResponse(favorite.getId(), product.getId(), product.getName(), product.getBrand(),
                product.getCategory(), product.getImageUrl(), favorite.getCreatedAt());
    }
}
