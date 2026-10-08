package com.travelplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.travelplanner.controller.AdminController;
import com.travelplanner.entity.User;
import com.travelplanner.service.DestinationService;
import com.travelplanner.service.ReviewService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private DestinationService destinationService;

    @Mock
    private TripService tripService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;


    @Test
    void adminDashboard_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                adminController.adminDashboard(
                        session,
                        model
                );

        assertEquals(
                "redirect:/login",
                result
        );

        verifyNoInteractions(userService);
        verifyNoInteractions(destinationService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(reviewService);
        verifyNoInteractions(model);
    }


    @Test
    void adminDashboard_shouldRedirectToDashboard_whenUserIsNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                adminController.adminDashboard(
                        session,
                        model
                );

        assertEquals(
                "redirect:/dashboard",
                result
        );

        verify(userService)
                .findById(1L);

        verifyNoInteractions(destinationService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(reviewService);
        verifyNoInteractions(model);
    }


    @Test
    void adminDashboard_shouldRedirectToDashboard_whenUserIsNotAdmin() {

        User user = new User();
        user.setId(1L);
        user.setRole("USER");

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        String result =
                adminController.adminDashboard(
                        session,
                        model
                );

        assertEquals(
                "redirect:/dashboard",
                result
        );

        verify(userService)
                .findById(1L);

        verifyNoInteractions(destinationService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(reviewService);
        verifyNoInteractions(model);
    }


    @Test
    void adminDashboard_shouldReturnAdminView_whenUserIsAdmin() {

        User admin = new User();
        admin.setId(1L);
        admin.setRole("ADMIN");

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(admin);

        String result =
                adminController.adminDashboard(
                        session,
                        model
                );

        assertEquals(
                "admin",
                result
        );

        verify(userService)
                .findById(1L);

        verify(userService)
                .getAllUsers();

        verify(destinationService)
                .getAllDestinations();

        verify(tripService)
                .getAllTrips();

        verify(reviewService)
                .getAllReviews();

        verify(model)
                .addAttribute(
                        eq("users"),
                        any()
                );

        verify(model)
                .addAttribute(
                        eq("destinations"),
                        any()
                );

        verify(model)
                .addAttribute(
                        eq("trips"),
                        any()
                );

        verify(model)
                .addAttribute(
                        eq("reviews"),
                        any()
                );
    }


    @Test
    void adminDashboard_shouldAllowAdminRoleIgnoringCase() {

        User admin = new User();
        admin.setId(1L);
        admin.setRole("admin");

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(admin);

        String result =
                adminController.adminDashboard(
                        session,
                        model
                );

        assertEquals(
                "admin",
                result
        );

        verify(userService)
                .getAllUsers();

        verify(destinationService)
                .getAllDestinations();

        verify(tripService)
                .getAllTrips();

        verify(reviewService)
                .getAllReviews();
    }
}