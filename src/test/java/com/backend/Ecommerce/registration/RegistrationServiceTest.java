package com.backend.Ecommerce.registration;

import com.backend.Ecommerce.appuser.AppUser;
import com.backend.Ecommerce.appuser.AppUserService;
import com.backend.Ecommerce.email.EmailSender;
import com.backend.Ecommerce.email.EmailValidator;
import com.backend.Ecommerce.email.template.EmailTemplateServiceImpl;
import com.backend.Ecommerce.registration.token.ConfirmationToken;
import com.backend.Ecommerce.registration.token.ConfirmationTokenService;
import com.backend.Ecommerce.registration.user.UserProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private AppUserService appUserService;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private EmailSender emailSender;

    @Mock
    private UserProducer userProducer;

    @Mock
    private EmailTemplateServiceImpl emailTemplateService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void register_shouldReturnToken_whenEmailIsValid() {
        RegistrationRequest request = new RegistrationRequest("John", "Doe", "password123", "john@example.com");

        when(emailValidator.test(request.getEmail())).thenReturn(true);
        when(appUserService.signUpUser(any(AppUser.class))).thenReturn("token123");

        when(emailTemplateService.buildEmail(anyString(), anyString())).thenReturn("mocked-email-content");

        String token = registrationService.register(request);

        assertEquals("token123", token);
        verify(emailSender).send(eq(request.getEmail()), anyString());
    }

    @Test
    void register_shouldThrowException_whenEmailInvalid() {
        RegistrationRequest request = new RegistrationRequest(
                "John", "Doe", "password123", "invalid-email"
        );
        when(emailValidator.test(request.getEmail())).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.register(request);
        });
        assertEquals("Invalid email address", exception.getMessage());

        verifyNoInteractions(appUserService);
        verifyNoInteractions(emailSender);
    }

    @Test
    void confirmToken_shouldConfirmToken_whenValid() {
        String token = "token123";
        AppUser appUser = new AppUser();
        appUser.setEmail("john@example.com");

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(10),
                appUser
        );

        when(confirmationTokenService.getToken(token)).thenReturn(java.util.Optional.of(confirmationToken));
        confirmationToken.setConfirmedAt(null);
        when(confirmationTokenService.setConfirmedAt(token)).thenReturn(1);
        when(appUserService.enableAppUser(appUser.getEmail())).thenReturn(1);

        String result = registrationService.confirmToken(token);

        assertEquals("REGISTRATION CONFIRMED!", result);
        verify(confirmationTokenService).setConfirmedAt(token);
        verify(appUserService).enableAppUser(appUser.getEmail());
    }

    @Test
    void confirmToken_shouldThrowException_whenTokenNotFound() {
        String token = "notfoundtoken";
        when(confirmationTokenService.getToken(token)).thenReturn(java.util.Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("token not found!", exception.getMessage());
    }

    @Test
    void confirmToken_shouldThrowException_whenAlreadyConfirmed() {
        String token = "token123";
        ConfirmationToken tokenObj = new ConfirmationToken();
        tokenObj.setConfirmedAt(LocalDateTime.now());
        when(confirmationTokenService.getToken(token)).thenReturn(java.util.Optional.of(tokenObj));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("email has already been confirmed!", exception.getMessage());
    }

    @Test
    void confirmToken_shouldThrowException_whenTokenExpired() {
        String token = "token123";
        ConfirmationToken tokenObj = new ConfirmationToken();
        tokenObj.setConfirmedAt(null);
        tokenObj.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        when(confirmationTokenService.getToken(token)).thenReturn(java.util.Optional.of(tokenObj));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token);
        });

        assertEquals("token has expired!", exception.getMessage());
    }
}
