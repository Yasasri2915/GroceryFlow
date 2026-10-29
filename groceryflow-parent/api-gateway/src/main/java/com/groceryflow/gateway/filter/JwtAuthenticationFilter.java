package com.groceryflow.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/actuator"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        // 1. Allow preflight OPTIONS requests
        if (HttpMethod.OPTIONS.equals(method)) {
            return chain.filter(exchange);
        }

        // 2. Allow public auth endpoints and public catalog GET endpoints
        if (isPublicEndpoint(path, method)) {
            // If token is present, we still enrich headers optionally
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    Claims claims = extractClaims(authHeader.substring(7));
                    ServerHttpRequest mutatedRequest = mutateRequestWithClaims(request, claims);
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                } catch (Exception ignored) {
                    // Public endpoint allows proceeding without valid token
                }
            }
            return chain.filter(exchange);
        }

        // 3. Protected endpoint: require Bearer token
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Authorization token is missing", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7).trim();
        try {
            Claims claims = extractClaims(token);
            ServerHttpRequest mutatedRequest = mutateRequestWithClaims(request, claims);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return onError(exchange, "Invalid or expired token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isPublicEndpoint(String path, HttpMethod method) {
        // Public Auth
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (path.startsWith(endpoint)) {
                return true;
            }
        }
        // Public Catalog Browsing (GET only)
        if (HttpMethod.GET.equals(method) && (path.startsWith("/api/products") || path.startsWith("/api/products/"))) {
            return true;
        }
        return false;
    }

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private ServerHttpRequest mutateRequestWithClaims(ServerHttpRequest request, Claims claims) {
        String userId = claims.get("userId") != null ? String.valueOf(claims.get("userId")) : "";
        String email = claims.getSubject() != null ? claims.getSubject() : "";
        String role = claims.get("role") != null ? String.valueOf(claims.get("role")) : "";

        return request.mutate()
                .header("X-User-Id", userId)
                .header("X-User-Email", email)
                .header("X-User-Role", role)
                .build();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String jsonError = String.format("{\"timestamp\":%d,\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                System.currentTimeMillis(), status.value(), status.getReasonPhrase(), message);

        byte[] bytes = jsonError.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100; // Run ahead of standard filters
    }
}
