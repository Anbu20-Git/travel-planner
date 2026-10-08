package com.travelplanner.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getWeather(String city) {

        String url =
                "https://wttr.in/"
                + city
                + "?format=3";

        return restTemplate.getForObject(
                url,
                String.class
        );
    }
}