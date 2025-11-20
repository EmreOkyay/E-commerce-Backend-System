package com.backend.Ecommerce.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private int status;
    private String error;
    private String code;
    private List<String> details;
}