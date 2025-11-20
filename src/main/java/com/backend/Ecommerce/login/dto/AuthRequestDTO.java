package com.backend.Ecommerce.login.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String username;
    private String password;
}