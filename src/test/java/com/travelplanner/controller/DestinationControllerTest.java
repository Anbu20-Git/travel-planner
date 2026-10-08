package com.travelplanner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        assertEquals(
                "destinations",
                result
        );

        verify(destinationService)
                .getAllDestinations();

        verify(model).addAttribute(
                "destinations",
                destinations
        );
    }
}