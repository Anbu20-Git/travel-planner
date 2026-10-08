package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.travelplanner.service.WeatherService;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getWeather_shouldReturnWeatherResponse() {

        String city = "Chennai";

        String expectedResponse =
                "Chennai: +32°C";

        String url =
                "https://wttr.in/"
                + city
                + "?format=3";

        when(restTemplate.getForObject(
                url,
                String.class
        )).thenReturn(expectedResponse);

        String result =
                weatherService.getWeather(city);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(restTemplate)
                .getForObject(url, String.class);
    }
}