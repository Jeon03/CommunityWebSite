package com.community.backend.dto.Login;


import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
