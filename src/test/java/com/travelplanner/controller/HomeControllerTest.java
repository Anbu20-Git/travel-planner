package com.travelplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    private final HomeController homeController =
            new HomeController();

    @Test
    void home_shouldReturnIndexView() {

        String result =
                homeController.home();

        assertEquals(
                "index",
                result
        );
    }

    @Test
    void login_shouldReturnLoginView() {

        String result =
                homeController.login();

        assertEquals(
                "login",
                result
        );
    }

    @Test
    void register_shouldReturnRegisterView() {

        String result =
                homeController.register();

        assertEquals(
                "register",
                result
        );
    }
}