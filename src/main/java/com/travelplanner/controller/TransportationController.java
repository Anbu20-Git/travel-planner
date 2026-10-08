package com.travelplanner.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.Transportation;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.TransportationService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransportationController {

    private final TransportationService transportationService;
    private final TripService tripService;
    private final UserService userService;

    public TransportationController(
            TransportationService transportationService,
            TripService tripService,
            UserService userService) {

        this.transportationService = transportationService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/transportation")
    public String transportation(
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
        // Only the owner of the trip can view transportation.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        List<Transportation> transportationList =
                transportationService.getTransportationByTrip(trip);

        model.addAttribute("trip", trip);
        model.addAttribute(
                "transportations",
                transportationList
        );

        return "transportation";
    }

    @PostMapping("/transportation/create")
    public String createTransportation(
            @RequestParam Long tripId,
            @RequestParam String type,
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String departureTime,
            @RequestParam String arrivalTime,
            @RequestParam BigDecimal cost,
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
        // Only the owner of the trip can add transportation.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        Transportation transportation =
                new Transportation();

        transportation.setTrip(trip);
        transportation.setType(type);
        transportation.setSource(source);
        transportation.setDestination(destination);

        transportation.setDepartureTime(
                LocalDateTime.parse(departureTime)
        );

        transportation.setArrivalTime(
                LocalDateTime.parse(arrivalTime)
        );

        transportation.setCost(cost);

        transportationService.saveTransportation(
                transportation
        );

        redirectAttributes.addFlashAttribute(
                "success",
                "Transportation added successfully!"
        );

        return "redirect:/transportation?tripId=" + tripId;
    }
}