package com.backend.Ecommerce.appuser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}

