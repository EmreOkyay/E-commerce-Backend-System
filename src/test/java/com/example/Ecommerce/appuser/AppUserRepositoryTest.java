package com.example.Ecommerce.appuser;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void shouldReturnUser_ifUserEmailDoesExist() {
        // given
        AppUser appUser = new AppUser(
                "John",
                "Doe",
                "random.email@hotmail.com",
                "password",
                AppUserRole.USER
        );

        underTest.save(appUser);

        // when
        Optional<AppUser> foundUser = underTest.findByEmail("random.email@hotmail.com");

        // then
        assertTrue(foundUser.isPresent());
        assertEquals(appUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void shouldReturnFalse_ifUserEmailDoesNotExist() {
        // given
        String email = "random.email@hotmail.com";

        // when
        Optional<AppUser> foundUser = underTest.findByEmail("random.email@hotmail.com");

        // then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldEnableUser_ifUserWithEmailExists() {
        // given
        AppUser appUser = new AppUser(
                "John",
                "Doe",
                "random.email@hotmail.com",
                "password",
                AppUserRole.USER
        );

        underTest.save(appUser);

        // when
        assertFalse(appUser.isEnabled());

        underTest.enableAppUser(appUser.getEmail());

        entityManager.flush();
        entityManager.clear();

        Optional<AppUser> updatedUser = underTest.findByEmail(appUser.getEmail());

        // then
        assertTrue(updatedUser.isPresent());
        assertTrue(updatedUser.get().isEnabled());
    }

}