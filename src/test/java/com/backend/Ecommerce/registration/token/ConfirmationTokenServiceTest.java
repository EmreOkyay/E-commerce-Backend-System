package com.backend.Ecommerce.registration.token;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {


    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @InjectMocks
    private ConfirmationTokenService confirmationTokenService;

    @Test
    void itShouldSaveConfirmationToken() {
        // given
        ConfirmationToken token = new ConfirmationToken();
        // when
        confirmationTokenService.saveConfirmationToken(token);
        // then
        verify(confirmationTokenRepository).save(token);
    }

    @Test
    void itShouldReturnTokenIfExists() {
        // given
        String tokenValue = "abc123";
        ConfirmationToken token = new ConfirmationToken();
        when(confirmationTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        // when
        Optional<ConfirmationToken> result = confirmationTokenService.getToken(tokenValue);

        // then
        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    void itShouldSetConfirmedAt() {
        // given
        String tokenValue = "abc123";
        when(confirmationTokenRepository.updateConfirmedAt(anyString(), any()))
                .thenReturn(1);

        // when
        int updated = confirmationTokenService.setConfirmedAt(tokenValue);

        // then
        assertEquals(1, updated);
        verify(confirmationTokenRepository).updateConfirmedAt(eq(tokenValue), any(LocalDateTime.class));
    }
}