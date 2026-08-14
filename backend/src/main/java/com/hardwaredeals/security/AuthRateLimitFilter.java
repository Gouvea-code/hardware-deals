package com.hardwaredeals.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AuthRateLimitFilter extends OncePerRequestFilter {
    private record Window(long minute, AtomicInteger count) {}
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();
    private final int limit;
    private final StringRedisTemplate redis;
    private final boolean distributed;

    public AuthRateLimitFilter(@Value("${app.auth.rate-limit-per-minute:10}") int limit,
                               @Value("${app.auth.distributed-rate-limit-enabled:true}") boolean distributed,
                               StringRedisTemplate redis) {
        if (limit < 1) throw new IllegalArgumentException("Rate limit must be positive");
        this.limit = limit;
        this.distributed = distributed;
        this.redis = redis;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/v1/auth/") || !"POST".equals(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        long minute = Instant.now().getEpochSecond() / 60;
        String identity = request.getRemoteAddr() + ':' + request.getRequestURI();
        if (requestCount(identity, minute) > limit) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.setHeader("Retry-After", "60");
            response.getWriter().write("{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Limite de tentativas excedido\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    private long requestCount(String identity, long minute) {
        if (!distributed) return localCount(identity, minute);
        String redisKey = "security:auth-rate:" + minute + ':' + Integer.toHexString(identity.hashCode());
        try {
            Long count = redis.opsForValue().increment(redisKey);
            if (count != null && count == 1L) redis.expire(redisKey, Duration.ofMinutes(2));
            return count == null ? localCount(identity, minute) : count;
        } catch (DataAccessException ex) {
            return localCount(identity, minute);
        }
    }

    private long localCount(String identity, long minute) {
        Window window = windows.compute(identity, (key, current) -> current == null || current.minute() != minute
                ? new Window(minute, new AtomicInteger(1)) : increment(current));
        return window.count().get();
    }

    private Window increment(Window window) { window.count().incrementAndGet(); return window; }
}
