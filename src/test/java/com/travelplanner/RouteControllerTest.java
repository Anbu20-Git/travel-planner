package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.controller.RouteController;
import com.travelplanner.entity.Route;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.RouteService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @Mock
    private TripService tripService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private RouteController routeController;


    @Test
    void routes_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                routeController.routes(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(routeService);
    }


    @Test
    void routes_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                routeController.routes(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);

        verifyNoInteractions(tripService);
        verifyNoInteractions(routeService);
    }


    @Test
    void routes_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                routeController.routes(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verify(tripService).findById(10L);

        verifyNoInteractions(routeService);
    }


    @Test
    void routes_shouldRedirectToTrips_whenUserIsNotTripOwner() {

        User loggedInUser = new User();
        loggedInUser.setId(1L);

        User tripOwner = new User();
        tripOwner.setId(2L);

        Trip trip = new Trip();
        trip.setUser(tripOwner);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(loggedInUser);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                routeController.routes(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(routeService);
    }


    @Test
    void routes_shouldReturnRoutesView_whenUserOwnsTrip() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        List<Route> routes =
                List.of(new Route());

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        when(routeService.getRoutesByTrip(trip))
                .thenReturn(routes);

        String result =
                routeController.routes(
                        10L,
                        session,
                        model
                );

        assertEquals("routes", result);

        verify(routeService)
                .getRoutesByTrip(trip);

        verify(model).addAttribute(
                "trip",
                trip
        );

        verify(model).addAttribute(
                "routes",
                routes
        );
    }


    @Test
    void createRoute_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                routeController.createRoute(
                        10L,
                        "Chennai",
                        "Pondicherry",
                        "CAR",
                        BigDecimal.valueOf(160),
                        "3 hours",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(routeService);
    }


    @Test
    void createRoute_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                routeController.createRoute(
                        10L,
                        "Chennai",
                        "Pondicherry",
                        "CAR",
                        BigDecimal.valueOf(160),
                        "3 hours",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);

        verifyNoInteractions(tripService);
        verifyNoInteractions(routeService);
    }


    @Test
    void createRoute_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                routeController.createRoute(
                        10L,
                        "Chennai",
                        "Pondicherry",
                        "CAR",
                        BigDecimal.valueOf(160),
                        "3 hours",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(routeService);
    }


    @Test
    void createRoute_shouldRedirectToTrips_whenUserIsNotTripOwner() {

        User loggedInUser = new User();
        loggedInUser.setId(1L);

        User tripOwner = new User();
        tripOwner.setId(2L);

        Trip trip = new Trip();
        trip.setUser(tripOwner);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(loggedInUser);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                routeController.createRoute(
                        10L,
                        "Chennai",
                        "Pondicherry",
                        "CAR",
                        BigDecimal.valueOf(160),
                        "3 hours",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(routeService);
    }


    @Test
    void createRoute_shouldSaveRoute_whenUserOwnsTrip() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        BigDecimal distance =
                BigDecimal.valueOf(160);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                routeController.createRoute(
                        10L,
                        "Chennai",
                        "Pondicherry",
                        "CAR",
                        distance,
                        "3 hours",
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/routes?tripId=10",
                result
        );

        ArgumentCaptor<Route> captor =
                ArgumentCaptor.forClass(Route.class);

        verify(routeService)
                .saveRoute(captor.capture());

        Route savedRoute =
                captor.getValue();

        assertEquals(
                trip,
                savedRoute.getTrip()
        );

        assertEquals(
                "Chennai",
                savedRoute.getSource()
        );

        assertEquals(
                "Pondicherry",
                savedRoute.getDestination()
        );

        assertEquals(
                "CAR",
                savedRoute.getTravelMode()
        );

        assertEquals(
                distance,
                savedRoute.getDistance()
        );

        assertEquals(
                "3 hours",
                savedRoute.getEstimatedTime()
        );

        verify(redirectAttributes)
                .addFlashAttribute(
                        "success",
                        "Route added successfully!"
                );
    }
}