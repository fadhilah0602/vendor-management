package com.example.vendor.controller;

import com.example.vendor.dto.BaseResponse;
import com.example.vendor.dto.LoginRequest;
import com.example.vendor.dto.LoginResponse;
import com.example.vendor.dto.UserDto;
import com.example.vendor.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@RequestBody UserDto userDto) {
        BaseResponse<Object> response = userService.register(userDto.getEmail(), userDto.getPassword());
        if ("200".equals(response.getCode())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }


}
