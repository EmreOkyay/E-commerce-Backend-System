package com.example.Ecommerce.order;

import com.example.Ecommerce.appuser.AppUser;
import com.example.Ecommerce.appuser.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestParam String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepository.findAllByUser(user);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestParam String email
    ) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderService.getOrderById(orderId, user);
        return ResponseEntity.ok(order);
    }


    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest user) {
        orderService.createOrder(user);
        return ResponseEntity.ok("Sipariş başarıyla oluşturuldu.");
    }
}
