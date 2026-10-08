package com.travelplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void dashboardWhenUserNotLoggedIn() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                dashboardController.dashboard(session, model);

        assertEquals("redirect:/login", result);
    }

    @Test
    void dashboardWhenUserLoggedIn() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(session.getAttribute("loggedInUserName"))
                .thenReturn("Anbu Selvan");

        String result =
                dashboardController.dashboard(session, model);

        assertEquals("dashboard", result);

        verify(model).addAttribute(
                "userName",
                "Anbu Selvan"
        );
    }

    @Test
    void logout() {

        String result =
                dashboardController.logout(session);

        verify(session).invalidate();

        assertEquals("redirect:/", result);
    }
}