package com.travelplanner.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.Destination;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.DestinationService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TripController {

    private final TripService tripService;
    private final DestinationService destinationService;
    private final UserService userService;

    public TripController(
            TripService tripService,
            DestinationService destinationService,
            UserService userService) {

        this.tripService = tripService;
        this.destinationService = destinationService;
        this.userService = userService;
    }

    @GetMapping("/trips")
    public String trips(
            HttpSession session,
            Model model) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user =
                userService.findById((Long) userId);

        if (user == null) {
            return "redirect:/login";
        }

        // Show only the logged-in user's trips
        model.addAttribute(
                "trips",
                tripService.getTripsByUser(user)
        );

        // Load destinations for Create Trip form
        model.addAttribute(
                "destinations",
                destinationService.getAllDestinations()
        );

        return "trips";
    }

    @PostMapping("/trips/create")
    public String createTrip(
            @RequestParam String tripName,
            @RequestParam Long destinationId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam BigDecimal budget,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user =
                userService.findById((Long) userId);

        if (user == null) {
            return "redirect:/login";
        }

        Destination destination =
                destinationService.findById(destinationId);

        if (destination == null) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Destination not found!"
            );

            return "redirect:/trips";
        }

        Trip trip = new Trip();

        trip.setUser(user);
        trip.setDestination(destination);
        trip.setTripName(tripName);
        trip.setStartDate(LocalDate.parse(startDate));
        trip.setEndDate(LocalDate.parse(endDate));
        trip.setBudget(budget);
        trip.setStatus("PLANNED");
        trip.setPublic(false);

        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute(
                "success",
                "Trip created successfully!"
        );

        return "redirect:/trips";
    }
}