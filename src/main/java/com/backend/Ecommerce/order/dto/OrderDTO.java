package com.backend.Ecommerce.order.dto;

import com.backend.Ecommerce.appuser.dto.UserSummaryDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private UserSummaryDTO user;
    private String status;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;
}
