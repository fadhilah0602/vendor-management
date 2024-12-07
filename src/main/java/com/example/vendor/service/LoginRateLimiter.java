package com.example.vendor.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginRateLimiter {
    private Map<String, Integer> requestCounts = new HashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 5;

    public boolean isRateLimited(String ip) {
        int count = requestCounts.getOrDefault(ip, 0);
        if (count >= MAX_REQUESTS_PER_MINUTE) {
            return true;
        }
        requestCounts.put(ip, count + 1);
        return false;
    }
}

