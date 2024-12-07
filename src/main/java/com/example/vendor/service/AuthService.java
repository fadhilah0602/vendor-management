package com.example.vendor.service;

import com.example.vendor.dto.BaseResponse;
import com.example.vendor.model.User;
import com.example.vendor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public BaseResponse<Object> register (String email, String password) {
        if(userRepository.findByEmail(email).isPresent()) {
            return BaseResponse.builder()
                    .code("409")
                    .status("Failed")
                    .message("Email is Already Registered")
                    .data(null)
                    .build();
        }

        String hadhedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hadhedPassword);
        userRepository.save(user);

        return BaseResponse.builder()
                .code("200")
                .status("Ok")
                .message("User Registered Successfully")
                .data(user.getEmail())
                .build();
    }

}
