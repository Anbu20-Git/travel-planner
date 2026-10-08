package com.travelplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.travelplanner.service.DestinationService;

@Controller
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/destinations")
    public String destinations(Model model) {

        model.addAttribute(
                "destinations",
                destinationService.getAllDestinations()
        );

        return "destinations";
    }
}