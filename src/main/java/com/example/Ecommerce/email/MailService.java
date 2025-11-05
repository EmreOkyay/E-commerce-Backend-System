package com.example.Ecommerce.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendOrderCreatedEmail(String toEmail, Long orderId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Sipariş Oluşturuldu");
            message.setText("Siparişiniz başarıyla oluşturuldu! Sipariş numaranız: " + orderId);
            mailSender.send(message);
            System.out.println("Mail sent to " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
