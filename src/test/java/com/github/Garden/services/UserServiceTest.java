package com.github.Garden.services;

import com.github.Garden.domain.Point;
import com.github.Garden.entities.Tree;
import com.github.Garden.entities.User;
import com.github.Garden.exceptions.EmailAlreadyRegisteredException;
import com.github.Garden.exceptions.ResourceNotFoundException;
import com.github.Garden.forms.RegisterForm;
import com.github.Garden.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterForm registerForm;

    @BeforeEach
    void setUp() {
        registerForm = new RegisterForm(
                "jhon",
                "jonjon@gmail.com",
                "Jhon",
                "Doe",
                "password"
        );
    }

    @Test
    @DisplayName("Creates a user from a valid register form.")
    void testRegister_validRegisterForm_savesUser() {
        // Given
        when(userRepository.existsByEmail(registerForm.email())).thenReturn(false);
        when(passwordEncoder.encode(registerForm.password())).thenReturn("encodedPassword123");

        // When
        userService.register(registerForm);

        // Then
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw a exception when the email is already registered")
    void testRegister_UserAlreadyRegistered() {
        // Given
        when(userRepository.existsByEmail(registerForm.email())).thenReturn(true);

        // When & Then
        assertThrows(EmailAlreadyRegisteredException.class, () -> {
            userService.register(registerForm);
        });
    }



}