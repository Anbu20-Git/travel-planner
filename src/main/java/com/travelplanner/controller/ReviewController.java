package com.travelplanner.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.Destination;
import com.travelplanner.entity.Review;
import com.travelplanner.entity.User;
import com.travelplanner.service.DestinationService;
import com.travelplanner.service.ReviewService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final DestinationService destinationService;
    private final UserService userService;

    public ReviewController(
            ReviewService reviewService,
            DestinationService destinationService,
            UserService userService) {

        this.reviewService = reviewService;
        this.destinationService = destinationService;
        this.userService = userService;
    }

    @GetMapping("/reviews")
    public String reviews(
            HttpSession session,
            Model model) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "reviews",
                reviewService.getAllReviews()
        );

        model.addAttribute(
                "destinations",
                destinationService.getAllDestinations()
        );

        return "reviews";
    }

    @PostMapping("/reviews/create")
    public String createReview(
            @RequestParam Long destinationId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user =
                userService.findById((Long) userId);

        Destination destination =
                destinationService.findById(destinationId);

        if (user == null || destination == null) {
            return "redirect:/reviews";
        }

        Review review = new Review();

        review.setUser(user);
        review.setDestination(destination);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());

        reviewService.saveReview(review);

        redirectAttributes.addFlashAttribute(
                "success",
                "Review submitted successfully!"
        );

        return "redirect:/reviews";
    }
    @PostMapping("/admin/reviews/delete")
    public String deleteReview(
            @RequestParam Long reviewId,
            HttpSession session) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user =
                userService.findById((Long) userId);

        if (user == null ||
            !"ADMIN".equalsIgnoreCase(user.getRole())) {

            return "redirect:/dashboard";
        }

        reviewService.deleteReview(reviewId);

        return "redirect:/admin";
    }
}