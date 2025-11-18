package com.example.Ecommerce.registration.user;

import com.example.Ecommerce.email.SmtpMailSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final SmtpMailSender smtpMailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @RabbitListener(queues = "userQueue")
    public void receiveUserMessage(String message) {
        try {
            UserMessage userMessage = objectMapper.readValue(message, UserMessage.class);

            if ("USER_ACTIVATED".equalsIgnoreCase(userMessage.getStatus())) {
                smtpMailSender.sendUserActivatedEmail(userMessage.getUserEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
