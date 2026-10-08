package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.controller.TripController;
import com.travelplanner.entity.Destination;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.DestinationService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock
    private TripService tripService;

    @Mock
    private DestinationService destinationService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private TripController tripController;


    @Test
    void trips_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                tripController.trips(
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(destinationService);
    }


    @Test
    void trips_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                tripController.trips(
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);
        verifyNoInteractions(tripService);
        verifyNoInteractions(destinationService);
    }


    @Test
    void trips_shouldReturnTripsView_whenUserExists() {

        User user = new User();
        user.setId(1L);

        List<Trip> trips =
                List.of(new Trip());

        List<Destination> destinations =
                List.of(new Destination());

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.getTripsByUser(user))
                .thenReturn(trips);

        when(destinationService.getAllDestinations())
                .thenReturn(destinations);

        String result =
                tripController.trips(
                        session,
                        model
                );

        assertEquals("trips", result);

        verify(model).addAttribute(
                "trips",
                trips
        );

        verify(model).addAttribute(
                "destinations",
                destinations
        );

        verify(tripService)
                .getTripsByUser(user);

        verify(destinationService)
                .getAllDestinations();
    }


    @Test
    void createTrip_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                tripController.createTrip(
                        "Chennai Trip",
                        10L,
                        "2026-10-10",
                        "2026-10-12",
                        BigDecimal.valueOf(10000),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(destinationService);
        verifyNoInteractions(tripService);
    }


    @Test
    void createTrip_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                tripController.createTrip(
                        "Chennai Trip",
                        10L,
                        "2026-10-10",
                        "2026-10-12",
                        BigDecimal.valueOf(10000),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);
        verifyNoInteractions(destinationService);
        verifyNoInteractions(tripService);
    }


    @Test
    void createTrip_shouldRedirectToTrips_whenDestinationNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(destinationService.findById(10L))
                .thenReturn(null);

        String result =
                tripController.createTrip(
                        "Chennai Trip",
                        10L,
                        "2026-10-10",
                        "2026-10-12",
                        BigDecimal.valueOf(10000),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verify(redirectAttributes)
                .addFlashAttribute(
                        "error",
                        "Destination not found!"
                );

        verifyNoInteractions(tripService);
    }


    @Test
    void createTrip_shouldSaveTrip_whenUserAndDestinationExist() {

        User user = new User();
        user.setId(1L);

        Destination destination = new Destination();
        destination.setId(10L);

        BigDecimal budget =
                BigDecimal.valueOf(10000);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(destinationService.findById(10L))
                .thenReturn(destination);

        String result =
                tripController.createTrip(
                        "Chennai Trip",
                        10L,
                        "2026-10-10",
                        "2026-10-12",
                        budget,
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        ArgumentCaptor<Trip> captor =
                ArgumentCaptor.forClass(Trip.class);

        verify(tripService)
                .saveTrip(captor.capture());

        Trip savedTrip =
                captor.getValue();

        assertEquals(user, savedTrip.getUser());
        assertEquals(
                destination,
                savedTrip.getDestination()
        );
        assertEquals(
                "Chennai Trip",
                savedTrip.getTripName()
        );
        assertEquals(
                LocalDate.of(2026, 10, 10),
                savedTrip.getStartDate()
        );
        assertEquals(
                LocalDate.of(2026, 10, 12),
                savedTrip.getEndDate()
        );
        assertEquals(
                budget,
                savedTrip.getBudget()
        );
        assertEquals(
                "PLANNED",
                savedTrip.getStatus()
        );
        assertFalse(savedTrip.isPublic());

        verify(redirectAttributes)
                .addFlashAttribute(
                        "success",
                        "Trip created successfully!"
                );
    }
}