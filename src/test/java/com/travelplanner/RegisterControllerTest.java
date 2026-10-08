package com.travelplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.controller.RegisterController;
import com.travelplanner.entity.User;
import com.travelplanner.service.UserService;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private RegisterController registerController;


    @Test
    void registerUser_shouldRedirectToLogin_whenRegistrationIsSuccessful() {

        User savedUser = new User();

        when(userService.registerUser(any(User.class)))
                .thenReturn(savedUser);

        String result =
                registerController.registerUser(
                        "Anbu",
                        "anbu@gmail.com",
                        "password123",
                        redirectAttributes
                );

        assertEquals(
                "redirect:/login",
                result
        );

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userService)
                .registerUser(userCaptor.capture());

        User capturedUser =
                userCaptor.getValue();

        assertEquals(
                "Anbu",
                capturedUser.getName()
        );

        assertEquals(
                "anbu@gmail.com",
                capturedUser.getEmail()
        );

        assertEquals(
                "password123",
                capturedUser.getPassword()
        );

        verify(redirectAttributes)
                .addFlashAttribute(
                        "success",
                        "Registration successful! Please login."
                );
    }


    @Test
    void registerUser_shouldRedirectToRegister_whenEmailAlreadyExists() {

        when(userService.registerUser(any(User.class)))
                .thenReturn(null);

        String result =
                registerController.registerUser(
                        "Anbu",
                        "anbu@gmail.com",
                        "password123",
                        redirectAttributes
                );

        assertEquals(
                "redirect:/register",
                result
        );

        verify(userService)
                .registerUser(any(User.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        "error",
                        "Email already registered!"
                );
    }
}