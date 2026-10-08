package com.travelplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.controller.LoginController;
import com.travelplanner.entity.User;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private LoginController loginController;


    @Test
    void login_shouldRedirectToLogin_whenUserDoesNotExist() {

        when(userService.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        String result =
                loginController.login(
                        "test@gmail.com",
                        "password123",
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/login",
                result
        );

        verify(userService)
                .findByEmail("test@gmail.com");

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(session);

        verify(redirectAttributes)
                .addFlashAttribute(
                        "error",
                        "Invalid email or password!"
                );
    }


    @Test
    void login_shouldRedirectToLogin_whenPasswordIsIncorrect() {

        User user = new User();
        user.setId(1L);
        user.setName("Anbu");
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");

        when(userService.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongPassword",
                "encodedPassword"
        )).thenReturn(false);

        String result =
                loginController.login(
                        "test@gmail.com",
                        "wrongPassword",
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/login",
                result
        );

        verify(passwordEncoder)
                .matches(
                        "wrongPassword",
                        "encodedPassword"
                );

        verifyNoInteractions(session);

        verify(redirectAttributes)
                .addFlashAttribute(
                        "error",
                        "Invalid email or password!"
                );
    }


    @Test
    void login_shouldRedirectToDashboard_whenValidNormalUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Anbu");
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");

        when(userService.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "encodedPassword"
        )).thenReturn(true);

        String result =
                loginController.login(
                        "test@gmail.com",
                        "password123",
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/dashboard",
                result
        );

        verify(session)
                .setAttribute(
                        "loggedInUserId",
                        1L
                );

        verify(session)
                .setAttribute(
                        "loggedInUserName",
                        "Anbu"
                );

        verify(session)
                .setAttribute(
                        "loggedInUserEmail",
                        "test@gmail.com"
                );

        verify(passwordEncoder)
                .matches(
                        "password123",
                        "encodedPassword"
                );

        verifyNoInteractions(redirectAttributes);
    }


    @Test
    void login_shouldRedirectToAdmin_whenValidAdminUser() {

        User admin = new User();
        admin.setId(2L);
        admin.setName("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("encodedAdminPassword");
        admin.setRole("ADMIN");

        when(userService.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.of(admin));

        when(passwordEncoder.matches(
                "adminPassword",
                "encodedAdminPassword"
        )).thenReturn(true);

        String result =
                loginController.login(
                        "admin@gmail.com",
                        "adminPassword",
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/admin",
                result
        );

        verify(session)
                .setAttribute(
                        "loggedInUserId",
                        2L
                );

        verify(session)
                .setAttribute(
                        "loggedInUserName",
                        "Admin"
                );

        verify(session)
                .setAttribute(
                        "loggedInUserEmail",
                        "admin@gmail.com"
                );

        verify(passwordEncoder)
                .matches(
                        "adminPassword",
                        "encodedAdminPassword"
                );

        verifyNoInteractions(redirectAttributes);
    }


    @Test
    void login_shouldTreatAdminRoleCaseInsensitively() {

        User admin = new User();
        admin.setId(3L);
        admin.setName("Admin");
        admin.setEmail("admin2@gmail.com");
        admin.setPassword("encodedPassword");
        admin.setRole("admin");

        when(userService.findByEmail("admin2@gmail.com"))
                .thenReturn(Optional.of(admin));

        when(passwordEncoder.matches(
                "password",
                "encodedPassword"
        )).thenReturn(true);

        String result =
                loginController.login(
                        "admin2@gmail.com",
                        "password",
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/admin",
                result
        );

        verify(session)
                .setAttribute(
                        "loggedInUserId",
                        3L
                );
    }
}