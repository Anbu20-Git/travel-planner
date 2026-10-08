package com.travelplanner.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travelplanner.entity.Trip;
import com.travelplanner.entity.TripShare;
import com.travelplanner.service.TripService;
import com.travelplanner.service.TripShareService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TripShareController {

    private final TripShareService tripShareService;
    private final TripService tripService;

    public TripShareController(
            TripShareService tripShareService,
            TripService tripService) {

        this.tripShareService = tripShareService;
        this.tripService = tripService;
    }

    @PostMapping("/trip/share")
    public String shareTrip(
            @RequestParam Long tripId,
            HttpSession session,
            Model model) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        Trip trip =
                tripService.findById(tripId);

        if (trip == null) {
            return "redirect:/trips";
        }

        // Security check:
        // Only the owner of the trip can create a share link.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        TripShare tripShare =
                tripShareService.createShare(trip);

        String shareUrl =
                "http://localhost:8080/shared-trip/"
                + tripShare.getShareCode();

        model.addAttribute("trip", trip);
        model.addAttribute("shareUrl", shareUrl);

        return "trip-share";
    }

    @GetMapping("/shared-trip/{shareCode}")
    public String viewSharedTrip(
            @PathVariable String shareCode,
            Model model) {

        Optional<TripShare> tripShare =
                tripShareService.findByShareCode(shareCode);

        if (tripShare.isEmpty()) {
            return "redirect:/";
        }

        Trip trip =
                tripShare.get().getTrip();

        model.addAttribute("trip", trip);

        return "shared-trip";
    }
}