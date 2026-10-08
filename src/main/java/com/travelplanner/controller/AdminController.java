package com.travelplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.travelplanner.entity.User;
import com.travelplanner.service.DestinationService;
import com.travelplanner.service.ReviewService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final UserService userService;
    private final DestinationService destinationService;
    private final TripService tripService;
    private final ReviewService reviewService;

    public AdminController(
            UserService userService,
            DestinationService destinationService,
            TripService tripService,
            ReviewService reviewService) {

        this.userService = userService;
        this.destinationService = destinationService;
        this.tripService = tripService;
        this.reviewService = reviewService;
    }

    @GetMapping("/admin")
    public String adminDashboard(
            HttpSession session,
            Model model) {

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

        model.addAttribute(
                "users",
                userService.getAllUsers()
        );

        model.addAttribute(
                "destinations",
                destinationService.getAllDestinations()
        );

        model.addAttribute(
                "trips",
                tripService.getAllTrips()
        );

        model.addAttribute(
                "reviews",
                reviewService.getAllReviews()
        );

        return "admin";
    }
}