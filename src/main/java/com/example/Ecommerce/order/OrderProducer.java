package com.example.Ecommerce.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.amqp.core.Queue;

@Component
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private Queue queue;

    @Autowired
    public OrderProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.queue = queue;
    }

    public void sendOrderMessage(Order order) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(new OrderMessage(order.getId(), order.getUser().getEmail(), order.getStatus().name()));

            rabbitTemplate.convertAndSend(queue.getName(), jsonMessage);
            System.out.println("Sent order message: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
