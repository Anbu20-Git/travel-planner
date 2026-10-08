package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.controller.ItineraryController;
import com.travelplanner.entity.Itinerary;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.ItineraryService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ItineraryControllerTest {

    @Mock
    private ItineraryService itineraryService;

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
    private ItineraryController itineraryController;


    @Test
    void itinerary_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                itineraryController.itinerary(
                        1L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(itineraryService);
    }


    @Test
    void itinerary_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                itineraryController.itinerary(
                        1L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);
        verifyNoInteractions(tripService);
        verifyNoInteractions(itineraryService);
    }


    @Test
    void itinerary_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                itineraryController.itinerary(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verify(tripService).findById(10L);
        verifyNoInteractions(itineraryService);
    }


    @Test
    void itinerary_shouldRedirectToTrips_whenUserDoesNotOwnTrip() {

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
                itineraryController.itinerary(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(itineraryService);
    }


    @Test
    void itinerary_shouldReturnView_whenUserOwnsTrip() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        List<Itinerary> itineraries =
                List.of(new Itinerary());

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        when(itineraryService.getItinerariesByTrip(trip))
                .thenReturn(itineraries);

        String result =
                itineraryController.itinerary(
                        10L,
                        session,
                        model
                );

        assertEquals("itinerary", result);

        verify(model).addAttribute("trip", trip);

        verify(model).addAttribute(
                "itineraries",
                itineraries
        );

        verify(itineraryService)
                .getItinerariesByTrip(trip);
    }


    @Test
    void createItinerary_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                itineraryController.createItinerary(
                        10L,
                        1,
                        "Visit Museum",
                        "Chennai Museum",
                        "10:30",
                        "Visit the museum",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(itineraryService);
    }


    @Test
    void createItinerary_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                itineraryController.createItinerary(
                        10L,
                        1,
                        "Visit Museum",
                        "Chennai Museum",
                        "10:30",
                        "Visit the museum",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);
        verifyNoInteractions(tripService);
        verifyNoInteractions(itineraryService);
    }


    @Test
    void createItinerary_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                itineraryController.createItinerary(
                        10L,
                        1,
                        "Visit Museum",
                        "Chennai Museum",
                        "10:30",
                        "Visit the museum",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verify(tripService).findById(10L);
        verifyNoInteractions(itineraryService);
    }


    @Test
    void createItinerary_shouldRedirectToTrips_whenUserDoesNotOwnTrip() {

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
                itineraryController.createItinerary(
                        10L,
                        1,
                        "Visit Museum",
                        "Chennai Museum",
                        "10:30",
                        "Visit the museum",
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(itineraryService);
        verifyNoInteractions(redirectAttributes);
    }


    @Test
    void createItinerary_shouldSaveItinerary_whenUserOwnsTrip() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                itineraryController.createItinerary(
                        10L,
                        2,
                        "Visit Museum",
                        "Chennai Museum",
                        "10:30",
                        "Visit the museum",
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/itinerary?tripId=10",
                result
        );

        ArgumentCaptor<Itinerary> captor =
                ArgumentCaptor.forClass(Itinerary.class);

        verify(itineraryService)
                .saveItinerary(captor.capture());

        Itinerary savedItinerary =
                captor.getValue();

        assertEquals(trip, savedItinerary.getTrip());
        assertEquals(
                2,
                savedItinerary.getDayNumber()
        );
        assertEquals(
                "Visit Museum",
                savedItinerary.getActivity()
        );
        assertEquals(
                "Chennai Museum",
                savedItinerary.getLocation()
        );
        assertEquals(
                LocalTime.of(10, 30),
                savedItinerary.getActivityTime()
        );
        assertEquals(
                "Visit the museum",
                savedItinerary.getDescription()
        );

        verify(redirectAttributes)
                .addFlashAttribute(
                        "success",
                        "Itinerary activity added successfully!"
                );
    }
}