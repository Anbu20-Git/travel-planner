package com.travelplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travelplanner.service.WeatherService;

import jakarta.servlet.http.HttpSession;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String weather(
            @RequestParam(required = false) String city,
            HttpSession session,
            Model model) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        if (city != null && !city.trim().isEmpty()) {

            try {

                String weather =
                        weatherService.getWeather(city.trim());

                model.addAttribute("weather", weather);
                model.addAttribute("searchedCity", city);

            } catch (Exception e) {

                model.addAttribute(
                        "error",
                        "Unable to fetch weather. Please try again."
                );
            }
        }

        return "weather";
    }
}