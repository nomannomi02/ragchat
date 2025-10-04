package com.example.ragstorage.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // run before everything else
public class ApiKeyAndRateLimitFilter extends OncePerRequestFilter {

    @Value("${app.api.key:}")
    private String apiKey;

    @Value("${app.rate.requests-per-minute:100}")
    private int requestsPerMinute;

    private static class Window {
        AtomicInteger count = new AtomicInteger(0);
        Instant expiresAt;
        Window(Instant expiresAt) { this.expiresAt = expiresAt; }
    }

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    private static final String[] PUBLIC_PATHS = {
            "/swagger-ui", "/v3/api-docs", "/actuator/health"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ Skip public endpoints
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // ✅ API key authentication
        String keyHeader = request.getHeader("X-API-KEY");
        if (apiKey == null || apiKey.isBlank() || !Objects.equals(apiKey, keyHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: missing or invalid API key");
            return;
        }

        // ✅ Rate limiting
        String rateKey = keyHeader != null ? keyHeader : request.getRemoteAddr();
        Window w = windows.compute(rateKey, (k, old) -> {
            Instant now = Instant.now();
            if (old == null || old.expiresAt.isBefore(now)) {
                Window nw = new Window(now.plusSeconds(60));
                nw.count.set(1);
                return nw;
            } else {
                old.count.incrementAndGet();
                return old;
            }
        });

        if (w.count.get() > requestsPerMinute) {
            response.setStatus(429);
            response.getWriter().write("Too Many Requests");
            return;
        }

        // ✅ Set authenticated principal for Spring Security
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "api-key-user", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
