package com.hardwaredeals.controller;

import com.hardwaredeals.dto.AuthDtos.*;
import com.hardwaredeals.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(request));
    }
    @PostMapping("/login") public TokenResponse login(@Valid @RequestBody LoginRequest r) { return auth.login(r); }
    @PostMapping("/refresh") public TokenResponse refresh(@Valid @RequestBody RefreshRequest r) { return auth.refresh(r.refreshToken()); }
    @PostMapping("/logout") public MessageResponse logout(@Valid @RequestBody LogoutRequest r) { return auth.logout(r.refreshToken()); }
    @PostMapping("/forgot-password") public MessageResponse forgot(@Valid @RequestBody ForgotPasswordRequest r) { return auth.forgotPassword(r.email()); }
    @PostMapping("/reset-password") public MessageResponse reset(@Valid @RequestBody ResetPasswordRequest r) { return auth.resetPassword(r); }
    @PostMapping("/verify-email") public MessageResponse verify(@Valid @RequestBody VerifyEmailRequest r) { return auth.verifyEmail(r.token()); }
    @GetMapping("/me") public Map<String, String> me(Authentication authentication) { return Map.of("userId", authentication.getName()); }
    @DeleteMapping("/me")
    public MessageResponse deleteAccount(Authentication authentication,
                                         @Valid @RequestBody DeleteAccountRequest request) {
        return auth.deleteAccount(java.util.UUID.fromString(authentication.getName()), request.password());
    }
}
