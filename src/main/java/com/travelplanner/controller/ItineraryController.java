package com.travelplanner.controller;

import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.Itinerary;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.ItineraryService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final TripService tripService;
    private final UserService userService;

    public ItineraryController(
            ItineraryService itineraryService,
            TripService tripService,
            UserService userService) {

        this.itineraryService = itineraryService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/itinerary")
    public String itinerary(
            @RequestParam Long tripId,
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

        Trip trip =
                tripService.findById(tripId);

        if (trip == null) {
            return "redirect:/trips";
        }

        // Security check:
        // Only the owner of the trip can view its itinerary.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        model.addAttribute("trip", trip);

        model.addAttribute(
                "itineraries",
                itineraryService.getItinerariesByTrip(trip)
        );

        return "itinerary";
    }

    @PostMapping("/itinerary/create")
    public String createItinerary(
            @RequestParam Long tripId,
            @RequestParam Integer dayNumber,
            @RequestParam String activity,
            @RequestParam String location,
            @RequestParam String activityTime,
            @RequestParam String description,
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

        Trip trip =
                tripService.findById(tripId);

        if (trip == null) {
            return "redirect:/trips";
        }

        // Security check:
        // Only the owner of the trip can add itinerary activities.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        Itinerary itinerary = new Itinerary();

        itinerary.setTrip(trip);
        itinerary.setDayNumber(dayNumber);
        itinerary.setActivity(activity);
        itinerary.setLocation(location);

        itinerary.setActivityTime(
                LocalTime.parse(activityTime)
        );

        itinerary.setDescription(description);

        itineraryService.saveItinerary(itinerary);

        redirectAttributes.addFlashAttribute(
                "success",
                "Itinerary activity added successfully!"
        );

        return "redirect:/itinerary?tripId=" + tripId;
    }
}