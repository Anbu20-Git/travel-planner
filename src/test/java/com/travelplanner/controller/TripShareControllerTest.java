package com.travelplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.travelplanner.entity.Trip;
import com.travelplanner.entity.TripShare;
import com.travelplanner.entity.User;
import com.travelplanner.service.TripService;
import com.travelplanner.service.TripShareService;

@ExtendWith(MockitoExtension.class)
class TripShareControllerTest {

    @Mock
    private TripShareService tripShareService;

    @Mock
    private TripService tripService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private TripShareController tripShareController;

    private User user;
    private Trip trip;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        trip = new Trip();
        trip.setUser(user);
    }

    @Test
    void shareTripWithoutLoginRedirectsToLogin() {
        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result = tripShareController.shareTrip(
                1L, session, model);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(tripService, tripShareService);
    }

    @Test
    void shareTripWhenTripDoesNotExistRedirectsToTrips() {
        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);
        when(tripService.findById(1L))
                .thenReturn(null);

        String result = tripShareController.shareTrip(
                1L, session, model);

        assertEquals("redirect:/trips", result);
        verify(tripShareService, never())
                .createShare(any(Trip.class));
    }

    @Test
    void shareTripWhenUserIsNotOwnerRedirectsToTrips() {
        when(session.getAttribute("loggedInUserId"))
                .thenReturn(2L);
        when(tripService.findById(1L))
                .thenReturn(trip);

        String result = tripShareController.shareTrip(
                1L, session, model);

        assertEquals("redirect:/trips", result);
        verify(tripShareService, never())
                .createShare(any(Trip.class));
    }

    @Test
    void shareTripWhenUserOwnsTripReturnsSharePage() {
        TripShare tripShare = new TripShare();
        tripShare.setTrip(trip);
        tripShare.setShareCode("abc123");

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);
        when(tripService.findById(1L))
                .thenReturn(trip);
        when(tripShareService.createShare(trip))
                .thenReturn(tripShare);

        String result = tripShareController.shareTrip(
                1L, session, model);

        assertEquals("trip-share", result);

        verify(model).addAttribute("trip", trip);
        verify(model).addAttribute(
                "shareUrl",
                "http://localhost:8080/shared-trip/abc123");
        verify(tripShareService).createShare(trip);
    }

    @Test
    void viewSharedTripWhenShareCodeDoesNotExistRedirectsHome() {
        when(tripShareService.findByShareCode("invalid"))
                .thenReturn(Optional.empty());

        String result = tripShareController.viewSharedTrip(
                "invalid", model);

        assertEquals("redirect:/", result);
        verify(model, never()).addAttribute(
                eq("trip"), any());
    }

    @Test
    void viewSharedTripWhenShareCodeExistsReturnsSharedTripPage() {
        TripShare tripShare = new TripShare();
        tripShare.setTrip(trip);

        when(tripShareService.findByShareCode("abc123"))
                .thenReturn(Optional.of(tripShare));

        String result = tripShareController.viewSharedTrip(
                "abc123", model);

        assertEquals("shared-trip", result);
        verify(model).addAttribute("trip", trip);
    }
}