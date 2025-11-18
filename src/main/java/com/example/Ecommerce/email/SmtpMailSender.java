package com.example.Ecommerce.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpMailSender {

    private final JavaMailSender mailSender;

    public void sendOrderCreatedEmail(String toEmail, Long orderId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order created");
            message.setText("Order successfully created! Order no: " + orderId);
            mailSender.send(message);
            System.out.println("Mail sent to " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUserActivatedEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your account is now active!");
        message.setText("Congratulations! Your account has been activated and you can now log in.");
        mailSender.send(message);
        System.out.println("Activation email sent to " + toEmail);
    }
}
