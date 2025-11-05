package com.example.Ecommerce.registration.user;

import com.example.Ecommerce.email.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final MailService mailService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @RabbitListener(queues = "userQueue")
    public void receiveUserMessage(String message) {
        try {
            UserMessage userMessage = objectMapper.readValue(message, UserMessage.class);

            if ("USER_ACTIVATED".equalsIgnoreCase(userMessage.getStatus())) {
                mailService.sendUserActivatedEmail(userMessage.getUserEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
