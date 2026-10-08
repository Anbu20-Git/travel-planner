package com.travelplanner.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    @Configuration
    static class RestTemplateConfig {

        @Bean
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}