package com.travelplanner.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.Route;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.RouteService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RouteController {

    private final RouteService routeService;
    private final TripService tripService;
    private final UserService userService;

    public RouteController(
            RouteService routeService,
            TripService tripService,
            UserService userService) {

        this.routeService = routeService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/routes")
    public String routes(
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
        // Only the owner of the trip can view its routes.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        List<Route> routes =
                routeService.getRoutesByTrip(trip);

        model.addAttribute("trip", trip);
        model.addAttribute("routes", routes);

        return "routes";
    }

    @PostMapping("/routes/create")
    public String createRoute(
            @RequestParam Long tripId,
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String travelMode,
            @RequestParam BigDecimal distance,
            @RequestParam String estimatedTime,
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
        // Only the owner of the trip can add routes.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        Route route = new Route();

        route.setTrip(trip);
        route.setSource(source);
        route.setDestination(destination);
        route.setTravelMode(travelMode);
        route.setDistance(distance);
        route.setEstimatedTime(estimatedTime);

        routeService.saveRoute(route);

        redirectAttributes.addFlashAttribute(
                "success",
                "Route added successfully!"
        );

        return "redirect:/routes?tripId=" + tripId;
    }
}