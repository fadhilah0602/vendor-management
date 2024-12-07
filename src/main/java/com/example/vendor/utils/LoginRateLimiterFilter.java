package com.example.vendor.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class LoginRateLimiterFilter extends OncePerRequestFilter {
    private final Cache<String, Long> requestCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    private static final int MAX_REQUESTS_PER_SECOND = 5;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr(); 
        long currentTime = System.currentTimeMillis();

        Long lastRequestTime = requestCache.getIfPresent(ip);

        if (lastRequestTime != null && (currentTime - lastRequestTime) < 1000) {
            response.setStatus(429);
            response.getWriter().write("Too many requests, please try again later.");
            return;
        }

        requestCache.put(ip, currentTime);

        filterChain.doFilter(request, response);
    }
}


