package com.example.Ecommerce.appuser;

import com.example.Ecommerce.registration.token.ConfirmationToken;
import com.example.Ecommerce.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void loadUserByUsername_shouldReturnUser_whenUserExists() {
        String email = "test@example.com";
        AppUser user = new AppUser();
        user.setEmail(email);

        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = appUserService.loadUserByUsername(email);

        assertEquals(email, result.getUsername());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        String email = "notfound@example.com";

        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.loadUserByUsername(email);
        });
    }

    @Test
    void signUpUser_shouldRegisterUser_whenUserDoesNotExist() {
        AppUser user = new AppUser();
        user.setEmail("new@example.com");
        user.setPassword("rawpassword");

        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedpassword");

        String token = appUserService.signUpUser(user);

        verify(appUserRepository).save(any(AppUser.class));
        verify(confirmationTokenService).saveConfirmationToken(any(ConfirmationToken.class));

        assertNotNull(token);
    }

    @Test
    void signUpUser_shouldThrowException_whenUserAlreadyExists() {
        AppUser user = new AppUser();
        user.setEmail("existing@example.com");

        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> {
            appUserService.signUpUser(user);
        });

        verify(appUserRepository, never()).save(any());
        verify(confirmationTokenService, never()).saveConfirmationToken(any());
    }

    @Test
    void enableAppUser_shouldReturnEnableResult() {
        String email = "enable@example.com";
        when(appUserRepository.enableAppUser(email)).thenReturn(1);

        int result = appUserService.enableAppUser(email);

        assertEquals(1, result);
    }
}