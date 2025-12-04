package com.backend.Ecommerce.email.template;

public interface EmailTemplateService {
    String buildEmail(String name, String link);
}
