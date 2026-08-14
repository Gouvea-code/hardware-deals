package com.hardwaredeals.service;

import com.hardwaredeals.dto.AuthDtos.*;
import com.hardwaredeals.entity.*;
import com.hardwaredeals.exception.ApiException;
import com.hardwaredeals.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.*;
import java.util.Base64;

@Service
@Transactional
public class AuthService {
    private static final Duration REFRESH_TTL = Duration.ofDays(30);
    private static final Duration ACTION_TTL = Duration.ofHours(1);
    private final UserRepository users;
    private final AuthTokenRepository tokens;
    private final PasswordEncoder passwords;
    private final JwtService jwt;
    private final EmailService emailService;
    private final DeviceTokenRepository deviceTokens;
    private final FavoriteRepository favorites;
    private final PriceAlertRepository alerts;
    private final NotificationRepository notifications;
    private final OfferClickRepository offerClicks;
    private final AnalyticsEventRepository analyticsEvents;
    private final SecureRandom random = new SecureRandom();

    public AuthService(UserRepository users, AuthTokenRepository tokens, PasswordEncoder passwords,
                       JwtService jwt, EmailService emailService, DeviceTokenRepository deviceTokens,
                       FavoriteRepository favorites, PriceAlertRepository alerts,
                       NotificationRepository notifications, OfferClickRepository offerClicks,
                       AnalyticsEventRepository analyticsEvents) {
        this.users = users;
        this.tokens = tokens;
        this.passwords = passwords;
        this.jwt = jwt;
        this.emailService = emailService;
        this.deviceTokens = deviceTokens;
        this.favorites = favorites;
        this.alerts = alerts;
        this.notifications = notifications;
        this.offerClicks = offerClicks;
        this.analyticsEvents = analyticsEvents;
    }

    public MessageResponse register(RegisterRequest request) {
        String normalizedEmail = normalize(request.email());
        if (users.existsByEmail(normalizedEmail)) throw new ApiException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        User user = users.save(User.builder().name(request.name().trim()).email(normalizedEmail)
                .passwordHash(passwords.encode(request.password())).status("ACTIVE").emailVerified(false).build());
        String raw = issue(user, AuthTokenType.EMAIL_VERIFICATION, ACTION_TTL);
        emailService.sendVerification(user.getEmail(), raw);
        return new MessageResponse("Cadastro realizado. Verifique seu e-mail para ativar o acesso.");
    }

    public TokenResponse login(LoginRequest request) {
        User user = users.findByEmail(normalize(request.email()))
                .orElseThrow(() -> unauthorized("Credenciais inválidas"));
        if (!passwords.matches(request.password(), user.getPasswordHash())) throw unauthorized("Credenciais inválidas");
        if (!"ACTIVE".equals(user.getStatus())) throw new ApiException(HttpStatus.FORBIDDEN, "Usuário inativo");
        if (!Boolean.TRUE.equals(user.getEmailVerified())) throw new ApiException(HttpStatus.FORBIDDEN, "E-mail ainda não verificado");
        return tokenPair(user);
    }

    public TokenResponse refresh(String rawToken) {
        AuthToken token = requireUsable(rawToken, AuthTokenType.REFRESH);
        token.setUsedAt(LocalDateTime.now());
        return tokenPair(token.getUser());
    }

    public MessageResponse logout(String rawToken) {
        tokens.findByTokenHashAndType(hash(rawToken), AuthTokenType.REFRESH)
                .filter(t -> t.getUsedAt() == null).ifPresent(t -> t.setUsedAt(LocalDateTime.now()));
        return new MessageResponse("Sessão encerrada");
    }

    public MessageResponse forgotPassword(String rawEmail) {
        users.findByEmail(normalize(rawEmail)).filter(u -> "ACTIVE".equals(u.getStatus())).ifPresent(user -> {
            String raw = issue(user, AuthTokenType.PASSWORD_RESET, ACTION_TTL);
            emailService.sendPasswordReset(user.getEmail(), raw);
        });
        return new MessageResponse("Se o e-mail existir, as instruções serão enviadas.");
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {
        AuthToken token = requireUsable(request.token(), AuthTokenType.PASSWORD_RESET);
        token.getUser().setPasswordHash(passwords.encode(request.newPassword()));
        token.setUsedAt(LocalDateTime.now());
        tokens.revokeActiveTokens(token.getUser().getId(), AuthTokenType.REFRESH, LocalDateTime.now());
        return new MessageResponse("Senha redefinida com sucesso");
    }

    public MessageResponse verifyEmail(String rawToken) {
        AuthToken token = requireUsable(rawToken, AuthTokenType.EMAIL_VERIFICATION);
        token.getUser().setEmailVerified(true);
        token.setUsedAt(LocalDateTime.now());
        return new MessageResponse("E-mail verificado com sucesso");
    }

    public MessageResponse deleteAccount(java.util.UUID userId, String password) {
        User user = users.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        if (!passwords.matches(password, user.getPasswordHash())) {
            throw unauthorized("Senha inválida");
        }
        analyticsEvents.deleteByUserId(userId);
        notifications.deleteByUserId(userId);
        offerClicks.deleteByUserId(userId);
        alerts.deleteByUserId(userId);
        favorites.deleteByUserId(userId);
        deviceTokens.deleteByUserId(userId);
        tokens.deleteByUserId(userId);
        users.delete(user);
        return new MessageResponse("Conta e dados pessoais excluídos");
    }

    private TokenResponse tokenPair(User user) {
        String refresh = issue(user, AuthTokenType.REFRESH, REFRESH_TTL);
        return new TokenResponse(jwt.createAccessToken(user), refresh, "Bearer", jwt.expiresInSeconds());
    }

    private String issue(User user, AuthTokenType type, Duration ttl) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokens.save(AuthToken.builder().user(user).tokenHash(hash(raw)).type(type)
                .expiresAt(LocalDateTime.now().plus(ttl)).build());
        return raw;
    }

    private AuthToken requireUsable(String raw, AuthTokenType type) {
        return tokens.findByTokenHashAndType(hash(raw), type).filter(t -> t.isUsable(LocalDateTime.now()))
                .orElseThrow(() -> unauthorized("Token inválido ou expirado"));
    }

    private String normalize(String email) { return email.trim().toLowerCase(java.util.Locale.ROOT); }
    private ApiException unauthorized(String message) { return new ApiException(HttpStatus.UNAUTHORIZED, message); }

    private String hash(String raw) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
