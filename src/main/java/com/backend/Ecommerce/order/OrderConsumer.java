package com.backend.Ecommerce.order;

import com.backend.Ecommerce.email.SmtpMailSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final SmtpMailSender smtpMailSender;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "orderQueue")
    public void receiveOrderMessage(String message) {
        try {
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            System.out.println("Received order message: " + orderMessage);

            if ("PENDING".equalsIgnoreCase(orderMessage.getStatus())) {
                smtpMailSender.sendOrderCreatedEmail(orderMessage.getUserEmail(), orderMessage.getOrderId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
