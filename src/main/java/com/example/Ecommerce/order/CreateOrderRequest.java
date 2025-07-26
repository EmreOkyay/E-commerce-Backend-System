package com.example.Ecommerce.order;

import lombok.Data;

import java.util.Map;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Map<Long, Integer> productIdQuantityMap;
}
