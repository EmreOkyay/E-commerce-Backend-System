package com.backend.Ecommerce.registration;

import com.backend.Ecommerce.appuser.AppUser;
import com.backend.Ecommerce.appuser.AppUserRole;
import com.backend.Ecommerce.appuser.AppUserService;
import com.backend.Ecommerce.email.EmailSender;
import com.backend.Ecommerce.email.EmailValidator;
import com.backend.Ecommerce.email.template.EmailTemplateService;
import com.backend.Ecommerce.registration.token.ConfirmationToken;
import com.backend.Ecommerce.registration.token.ConfirmationTokenService;
import com.backend.Ecommerce.registration.user.UserProducer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final UserProducer userProducer;
    private final EmailTemplateService emailTemplateService;
    EmailValidator validator = new EmailValidator();

    @Autowired
    public RegistrationService(AppUserService appUserService, EmailValidator emailValidator, ConfirmationTokenService confirmationTokenService, EmailSender emailSender, UserProducer userProducer, EmailTemplateService emailTemplateService) {
        this.appUserService = appUserService;
        this.emailValidator = emailValidator;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
        this.userProducer = userProducer;
        this.emailTemplateService = emailTemplateService;
    }

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("Invalid email address");
        }
        String token = appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;

        if (validator.test(request.getEmail()))
            emailSender.send(request.getEmail(), emailTemplateService.buildEmail(request.getFirstName(), link));
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found!"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email has already been confirmed!");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token has expired!");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        userProducer.sendUserMail(confirmationToken.getAppUser().getEmail());
        return "REGISTRATION CONFIRMED!";
    }
}
