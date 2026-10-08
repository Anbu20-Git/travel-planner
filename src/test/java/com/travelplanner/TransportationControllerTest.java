package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.controller.TransportationController;
import com.travelplanner.entity.Transportation;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.TransportationService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class TransportationControllerTest {

    @Mock
    private TransportationService transportationService;

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
    private TransportationController transportationController;


    @Test
    void transportation_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                transportationController.transportation(
                        1L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(transportationService);
    }


    @Test
    void transportation_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                transportationController.transportation(
                        1L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);
        verifyNoInteractions(tripService);
        verifyNoInteractions(transportationService);
    }


    @Test
    void transportation_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                transportationController.transportation(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verify(tripService).findById(10L);
        verifyNoInteractions(transportationService);
    }


    @Test
    void transportation_shouldRedirectToTrips_whenUserDoesNotOwnTrip() {

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
                transportationController.transportation(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(transportationService);
    }


    @Test
    void transportation_shouldReturnView_whenUserOwnsTrip() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        List<Transportation> transportationList =
                List.of(new Transportation());

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        when(transportationService.getTransportationByTrip(trip))
                .thenReturn(transportationList);

        String result =
                transportationController.transportation(
                        10L,
                        session,
                        model
                );

        assertEquals("transportation", result);

        verify(model).addAttribute("trip", trip);

        verify(model).addAttribute(
                "transportations",
                transportationList
        );

        verify(transportationService)
                .getTransportationByTrip(trip);
    }


    @Test
    void createTransportation_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                transportationController.createTransportation(
                        10L,
                        "Flight",
                        "Chennai",
                        "Delhi",
                        "2026-10-08T10:00",
                        "2026-10-08T12:00",
                        BigDecimal.valueOf(5000),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(transportationService);
    }


    @Test
    void createTransportation_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                transportationController.createTransportation(
                        10L,
                        "Flight",
                        "Chennai",
                        "Delhi",
                        "2026-10-08T10:00",
                        "2026-10-08T12:00",
                        BigDecimal.valueOf(5000),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);
        verifyNoInteractions(tripService);
        verifyNoInteractions(transportationService);
    }


    @Test
    void createTransportation_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                transportationController.createTransportation(
                        10L,
                        "Flight",
                        "Chennai",
                        "Delhi",
                        "2026-10-08T10:00",
                        "2026-10-08T12:00",
                        BigDecimal.valueOf(5000),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verify(tripService).findById(10L);
        verifyNoInteractions(transportationService);
    }


    @Test
    void createTransportation_shouldRedirectToTrips_whenUserDoesNotOwnTrip() {

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
                transportationController.createTransportation(
                        10L,
                        "Flight",
                        "Chennai",
                        "Delhi",
                        "2026-10-08T10:00",
                        "2026-10-08T12:00",
                        BigDecimal.valueOf(5000),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(transportationService);
        verifyNoInteractions(redirectAttributes);
    }


    @Test
    void createTransportation_shouldSaveTransportation_whenUserOwnsTrip() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        BigDecimal cost =
                BigDecimal.valueOf(5000);

        String departureTime =
                "2026-10-08T10:00";

        String arrivalTime =
                "2026-10-08T12:00";

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                transportationController.createTransportation(
                        10L,
                        "Flight",
                        "Chennai",
                        "Delhi",
                        departureTime,
                        arrivalTime,
                        cost,
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/transportation?tripId=10",
                result
        );

        verify(transportationService)
                .saveTransportation(any(Transportation.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        "success",
                        "Transportation added successfully!"
                );

        var captor =
                org.mockito.ArgumentCaptor.forClass(
                        Transportation.class
                );

        verify(transportationService)
                .saveTransportation(captor.capture());

        Transportation savedTransportation =
                captor.getValue();

        assertEquals(trip, savedTransportation.getTrip());
        assertEquals("Flight", savedTransportation.getType());
        assertEquals("Chennai", savedTransportation.getSource());
        assertEquals("Delhi", savedTransportation.getDestination());
        assertEquals(
                LocalDateTime.parse(departureTime),
                savedTransportation.getDepartureTime()
        );
        assertEquals(
                LocalDateTime.parse(arrivalTime),
                savedTransportation.getArrivalTime()
        );
        assertEquals(
                cost,
                savedTransportation.getCost()
        );
    }
}