package com.hardwaredeals.controller;

import com.hardwaredeals.dto.FavoriteDtos.FavoriteResponse;
import com.hardwaredeals.service.FavoriteService;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService favorites;
    public FavoriteController(FavoriteService favorites) { this.favorites = favorites; }
    @GetMapping public List<FavoriteResponse> list(Authentication auth) { return favorites.list(userId(auth)); }
    @PutMapping("/{productId}") public FavoriteResponse add(Authentication auth, @PathVariable UUID productId) {
        return favorites.add(userId(auth), productId);
    }
    @DeleteMapping("/{productId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(Authentication auth, @PathVariable UUID productId) { favorites.remove(userId(auth), productId); }
    private UUID userId(Authentication auth) { return UUID.fromString(auth.getName()); }
}
