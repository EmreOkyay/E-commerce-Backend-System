package com.example.Ecommerce.registration.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    private String userEmail;
    private String status;
}
