package com.backend.Ecommerce.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private Long orderId;
    private String userEmail;
    private String status;
}
