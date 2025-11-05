package com.example.Ecommerce.registration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.example.Ecommerce.security.config.RabbitMQConfig.*;

@Component
public class UserProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Queue queue;

    @Autowired
    public UserProducer(RabbitTemplate rabbitTemplate, @Qualifier("userQueue") Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void sendUserMail(String email) {
        try {
            UserMessage message = new UserMessage();
            message.setUserEmail(email);
            message.setStatus("USER_ACTIVATED");

            String json = objectMapper.writeValueAsString(message);

            rabbitTemplate.convertAndSend(USER_EXCHANGE, USER_ROUTING_KEY, json);
            System.out.println("Activation mail message sent for: " + email);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
