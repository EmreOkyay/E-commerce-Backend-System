package com.example.Ecommerce.order;

import com.example.Ecommerce.appuser.AppUser;
import com.example.Ecommerce.appuser.AppUserRepository;
import com.example.Ecommerce.cart.Cart;
import com.example.Ecommerce.cart.CartItem;
import com.example.Ecommerce.cart.CartRepository;
import com.example.Ecommerce.order.dto.DTOConverter;
import com.example.Ecommerce.order.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AppUserRepository appUserRepository;
    private final OrderProducer orderProducer;

    public void createOrder(CreateOrderRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId can not be null");
        }

        AppUser user = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Users cart could not be found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot create an order");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getProductPrice());

            order.getItems().add(orderItem);

            totalAmount = totalAmount.add(
                    item.getProduct().getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        orderProducer.sendOrderMessage(order);

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public List<OrderDTO> getOrders(AppUser user) {
        List<Order> orders = orderRepository.findAllByUser(user);
        return orders.stream()
                .map(DTOConverter::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long orderId, AppUser user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find the order"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("This order does not belong to you");
        }
        return DTOConverter.convertToOrderDTO(order);
    }
}
