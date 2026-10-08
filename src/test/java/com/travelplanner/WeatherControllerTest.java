package com.travelplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.travelplanner.controller.WeatherController;
import com.travelplanner.service.WeatherService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private WeatherController weatherController;


    @Test
    void weather_shouldRedirectToLogin_whenUserIsNotLoggedIn() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                weatherController.weather(
                        "Chennai",
                        session,
                        model
                );

        assertEquals(
                "redirect:/login",
                result
        );

        verifyNoInteractions(weatherService);
        verifyNoInteractions(model);
    }


    @Test
    void weather_shouldReturnWeatherPage_whenCityIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        String result =
                weatherController.weather(
                        null,
                        session,
                        model
                );

        assertEquals(
                "weather",
                result
        );

        verifyNoInteractions(weatherService);
        verifyNoInteractions(model);
    }


    @Test
    void weather_shouldReturnWeatherPage_whenCityIsEmpty() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        String result =
                weatherController.weather(
                        "",
                        session,
                        model
                );

        assertEquals(
                "weather",
                result
        );

        verifyNoInteractions(weatherService);
        verifyNoInteractions(model);
    }


    @Test
    void weather_shouldReturnWeatherPage_whenCityContainsOnlySpaces() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        String result =
                weatherController.weather(
                        "   ",
                        session,
                        model
                );

        assertEquals(
                "weather",
                result
        );

        verifyNoInteractions(weatherService);
        verifyNoInteractions(model);
    }


    @Test
    void weather_shouldFetchWeatherAndAddAttributes_whenCityIsValid() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(weatherService.getWeather("Chennai"))
                .thenReturn("Chennai: +31°C");

        String result =
                weatherController.weather(
                        "Chennai",
                        session,
                        model
                );

        assertEquals(
                "weather",
                result
        );

        verify(weatherService)
                .getWeather("Chennai");

        verify(model)
                .addAttribute(
                        "weather",
                        "Chennai: +31°C"
                );

        verify(model)
                .addAttribute(
                        "searchedCity",
                        "Chennai"
                );
    }


    @Test
    void weather_shouldTrimCityBeforeFetchingWeather() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(weatherService.getWeather("Chennai"))
                .thenReturn("Chennai: +31°C");

        String result =
                weatherController.weather(
                        "  Chennai  ",
                        session,
                        model
                );

        assertEquals(
                "weather",
                result
        );

        verify(weatherService)
                .getWeather("Chennai");

        verify(model)
                .addAttribute(
                        "weather",
                        "Chennai: +31°C"
                );

        verify(model)
                .addAttribute(
                        "searchedCity",
                        "  Chennai  "
                );
    }


    @Test
    void weather_shouldAddError_whenWeatherServiceThrowsException() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(weatherService.getWeather("Chennai"))
                .thenThrow(
                        new RuntimeException("Weather API failed")
                );

        String result =
                weatherController.weather(
                        "Chennai",
                        session,
                        model
                );

        assertEquals(
                "weather",
                result
        );

        verify(weatherService)
                .getWeather("Chennai");

        verify(model)
                .addAttribute(
                        "error",
                        "Unable to fetch weather. Please try again."
                );

        verify(model, never())
                .addAttribute(
                        eq("weather"),
                        any()
                );
    }
}