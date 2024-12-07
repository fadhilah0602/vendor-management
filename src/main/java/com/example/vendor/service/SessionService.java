package com.example.vendor.service;

import com.example.vendor.model.User;
import com.example.vendor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserLoggedIn(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getActiveToken() != null;
    }

    public void logUserIn(String email, String token) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setActiveToken(token);
        userRepository.save(user);
    }

    public void logUserOut(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setActiveToken(null);
        userRepository.save(user);
    }
}

