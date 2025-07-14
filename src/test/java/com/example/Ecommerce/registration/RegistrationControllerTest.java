package com.example.Ecommerce.registration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void itShouldRegisterUser() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "john",
                "doe",
                "john.doe@email.com",
                "password"
        );

        when(registrationService.register(request)).thenReturn("User Registered");

        // when
        String response = registrationController.register(request);

        // then
        assertEquals("User Registered", response);
    }

    @Test
    void itShouldConfirmToken() {
        // given
        String token = "abc123";
        when(registrationService.confirmToken(token)).thenReturn("Confirmed");

        // when
        String result = registrationController.confirm(token);

        // then
        assertEquals("Confirmed", result);
    }
}