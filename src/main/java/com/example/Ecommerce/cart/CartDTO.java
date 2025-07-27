package com.example.Ecommerce.cart;

import com.example.Ecommerce.appuser.dto.UserSummaryDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class CartDTO {
    private Long id;
    private UserSummaryDTO user;
    private List<CartItemDTO> items;
}

