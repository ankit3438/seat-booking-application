package com.example.apigateway.apigateway.Filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigateway.apigateway.Utils.JwtUtil;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // 🔓 Allow auth endpoints
        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Unauthorized - Missing or invalid Authorization token");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Unauthorized - Invalid token");
        }

        // ✅ Extract userId
        String userId = jwtUtil.extractUserId(token);

        // ✅ Inject header
        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("X-User-Id", userId)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> sendErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        String errorJson = "{\"error\": \"" + message + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorJson.getBytes());
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1; // run early
    }
}