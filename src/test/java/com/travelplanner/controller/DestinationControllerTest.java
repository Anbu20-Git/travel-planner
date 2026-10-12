package com.travelplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.travelplanner.entity.Destination;
import com.travelplanner.service.DestinationService;

@ExtendWith(MockitoExtension.class)
class DestinationControllerTest {

    @Mock
    private DestinationService destinationService;

    @Mock
    private Model model;

    @InjectMocks
    private DestinationController destinationController;

    @Test
    void destinations_shouldReturnDestinationsView() {

        List<Destination> destinations =
                List.of(
                        new Destination(),
                        new Destination()
                );

        when(destinationService.getAllDestinations())
                .thenReturn(destinations);

        String result =
                destinationController.destinations(model);

        assertEquals("destinations", result);

        verify(destinationService).getAllDestinations();

        verify(model).addAttribute(
                "destinations",
                destinations
        );
    }

    @Test
    void exploreDestination_shouldReturnExploreViewForAllDestinations() {

        String[][] destinations = {
                {"PARIS", "Paris", "France"},
                {"DUBAI", "Dubai", "United Arab Emirates"},
                {"BALI", "Bali", "Indonesia"},
                {"TOKYO", "Tokyo", "Japan"},
                {"LONDON", "London", "United Kingdom"},
                {"SINGAPORE", "Singapore", "Singapore"}
        };

        for (String[] destination : destinations) {

            clearInvocations(model);

            String destinationName = "  "
                    + destination[0]
                    + "  ";

            String result = destinationController.exploreDestination(
                    destinationName,
                    model
            );

            assertEquals("destination-explore", result);

            verify(model).addAttribute(
                    "destinationName",
                    destination[1]
            );

            verify(model).addAttribute(
                    "country",
                    destination[2]
            );

            verify(model).addAttribute(
                    eq("places"),
                    argThat(value ->
                            value instanceof List<?> list
                                    && list.size() == 10)
            );

            verify(model).addAttribute(
                    eq("foods"),
                    argThat(value ->
                            value instanceof List<?> list
                                    && list.size() == 10)
            );
        }
    }

    @Test
    void exploreDestination_shouldRedirectForInvalidDestination() {

        String result = destinationController.exploreDestination(
                "chennai",
                model
        );

        assertEquals("redirect:/destinations", result);
    }

    @Test
    void exploreDestination_shouldHandleUnknownDestination() {

        String result = destinationController.exploreDestination(
                "unknown-place",
                model
        );

        assertEquals("redirect:/destinations", result);
    }
}