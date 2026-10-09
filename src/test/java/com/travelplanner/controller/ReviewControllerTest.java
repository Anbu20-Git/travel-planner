package com.travelplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.Destination;
import com.travelplanner.entity.Review;
import com.travelplanner.entity.User;
import com.travelplanner.service.DestinationService;
import com.travelplanner.service.ReviewService;
import com.travelplanner.service.UserService;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

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
    private ReviewController reviewController;

    private User user;
    private Destination destination;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setRole("USER");

        destination = new Destination();
        destination.setId(1L);
    }

    @Test
    void reviewsWithoutLoginRedirectsToLogin() {
        when(session.getAttribute("loggedInUserId")).thenReturn(null);

        String result = reviewController.reviews(session, model);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(reviewService, destinationService, model);
    }

    @Test
    void reviewsWithLoginLoadsReviewsAndDestinations() {
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(reviewService.getAllReviews()).thenReturn(List.of());
        when(destinationService.getAllDestinations()).thenReturn(List.of());

        String result = reviewController.reviews(session, model);

        assertEquals("reviews", result);
        verify(model).addAttribute("reviews", List.of());
        verify(model).addAttribute("destinations", List.of());
    }

    @Test
    void createReviewWithoutLoginRedirectsToLogin() {
        when(session.getAttribute("loggedInUserId")).thenReturn(null);

        String result = reviewController.createReview(
                1L, 5, "Excellent", session, redirectAttributes);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(userService, destinationService, reviewService);
    }

    @Test
    void createReviewWhenUserDoesNotExistRedirectsToReviews() {
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(null);

        String result = reviewController.createReview(
                1L, 5, "Excellent", session, redirectAttributes);

        assertEquals("redirect:/reviews", result);
        verify(reviewService, never()).saveReview(any(Review.class));
    }

    @Test
    void createReviewWhenDestinationDoesNotExistRedirectsToReviews() {
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(user);
        when(destinationService.findById(2L)).thenReturn(null);

        String result = reviewController.createReview(
                2L, 5, "Excellent", session, redirectAttributes);

        assertEquals("redirect:/reviews", result);
        verify(reviewService, never()).saveReview(any(Review.class));
    }

    @Test
    void createReviewSavesReviewSuccessfully() {
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(user);
        when(destinationService.findById(1L)).thenReturn(destination);

        String result = reviewController.createReview(
                1L, 5, "Excellent", session, redirectAttributes);

        assertEquals("redirect:/reviews", result);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewService).saveReview(captor.capture());

        Review savedReview = captor.getValue();
        assertEquals(user, savedReview.getUser());
        assertEquals(destination, savedReview.getDestination());
        assertEquals(5, savedReview.getRating());
        assertEquals("Excellent", savedReview.getComment());
        assertNotNull(savedReview.getReviewDate());

        verify(redirectAttributes).addFlashAttribute(
                "success", "Review submitted successfully!");
    }

    @Test
    void deleteReviewWithoutLoginRedirectsToLogin() {
        when(session.getAttribute("loggedInUserId")).thenReturn(null);

        String result = reviewController.deleteReview(1L, session);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(userService, reviewService);
    }

    @Test
    void deleteReviewWhenUserDoesNotExistRedirectsToDashboard() {
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(null);

        String result = reviewController.deleteReview(1L, session);

        assertEquals("redirect:/dashboard", result);
        verify(reviewService, never()).deleteReview(anyLong());
    }

    @Test
    void deleteReviewForNonAdminRedirectsToDashboard() {
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(user);

        String result = reviewController.deleteReview(1L, session);

        assertEquals("redirect:/dashboard", result);
        verify(reviewService, never()).deleteReview(anyLong());
    }

    @Test
    void deleteReviewForAdminDeletesReview() {
        user.setRole("ADMIN");
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(user);

        String result = reviewController.deleteReview(5L, session);

        assertEquals("redirect:/admin", result);
        verify(reviewService).deleteReview(5L);
    }

    @Test
    void deleteReviewAllowsCaseInsensitiveAdminRole() {
        user.setRole("admin");
        when(session.getAttribute("loggedInUserId")).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(user);

        String result = reviewController.deleteReview(5L, session);

        assertEquals("redirect:/admin", result);
        verify(reviewService).deleteReview(5L);
    }
}