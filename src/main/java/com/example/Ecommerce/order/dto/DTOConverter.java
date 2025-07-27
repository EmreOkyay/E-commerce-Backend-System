package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.appuser.AppUser;
import com.example.Ecommerce.appuser.dto.UserSummaryDTO;
import com.example.Ecommerce.cart.Cart;
import com.example.Ecommerce.cart.dto.CartDTO;
import com.example.Ecommerce.cart.CartItem;
import com.example.Ecommerce.cart.dto.CartItemDTO;
import com.example.Ecommerce.order.Order;
import com.example.Ecommerce.order.OrderItem;
import com.example.Ecommerce.product.Product;
import com.example.Ecommerce.product.dto.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    public static OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUser(convertToUserSummaryDTO(order.getUser()));
        dto.setStatus(String.valueOf(order.getStatus()));
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(DTOConverter::convertToOrderItemDTO)
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);

        return dto;
    }

    public static UserSummaryDTO convertToUserSummaryDTO(AppUser appUser) {
        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(appUser.getId());
        dto.setFirstName(appUser.getFirstName());
        dto.setLastName(appUser.getLastName());
        dto.setEmail(appUser.getEmail());
        return dto;
    }

    public static OrderItemDTO convertToOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProduct(convertToProductDTO(item.getProduct()));
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }

    public static ProductDTO convertToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductCategory(product.getProductCategory());
        return dto;
    }

    public static CartDTO convertToCartDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUser(convertToUserSummaryDTO(cart.getUser()));

        List<CartItemDTO> items = cart.getItems().stream()
                .map(DTOConverter::convertToCartItemDTO)
                .collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }

    public static CartItemDTO convertToCartItemDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProduct(convertToProductDTO(item.getProduct()));
        dto.setQuantity(item.getQuantity());
        return dto;
    }

}

