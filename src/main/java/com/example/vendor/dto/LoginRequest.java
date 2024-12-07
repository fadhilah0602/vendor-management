package com.example.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class LoginRequest implements Serializable {


    private static final long serialVersionUID = -6228758807664321709L;
    private String email;
    private String password;
}
