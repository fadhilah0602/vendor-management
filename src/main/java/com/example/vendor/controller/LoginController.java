package com.example.vendor.controller;

import com.example.vendor.dto.LoginRequest;
import com.example.vendor.dto.LoginResponse;
import com.example.vendor.dto.UserDto;
import com.example.vendor.service.LoginRateLimiter;
import com.example.vendor.service.SessionService;
import com.example.vendor.service.UserDetailsServiceImpl;
import com.example.vendor.utils.JwtUtil;
import com.example.vendor.utils.LoginRateLimiterFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginRateLimiterFilter loginRateLimiterFilter;

    @Autowired
    private LoginRateLimiter loginRateLimiter;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/authentication")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String ip = request.getRemoteAddr();
        if (loginRateLimiter.isRateLimited(ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many requests, please try again later.");
        }

        if (sessionService.isUserLoggedIn(loginRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User is already logged in on another device.");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect Username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        sessionService.logUserIn(loginRequest.getEmail(), jwt);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserDto logoutRequest) {
        sessionService.logUserOut(logoutRequest.getEmail());
        return ResponseEntity.ok("User logged out successfully.");
    }

}
