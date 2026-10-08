package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travelplanner.entity.Destination;
import com.travelplanner.repository.DestinationRepository;
import com.travelplanner.service.DestinationService;

@ExtendWith(MockitoExtension.class)
class DestinationServiceTest {

    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationService destinationService;

    private Destination destination;

    @BeforeEach
    void setUp() {
        destination = new Destination();
        destination.setId(1L);
    }

    @Test
    void saveDestination_shouldSaveAndReturnDestination() {
        when(destinationRepository.save(destination))
                .thenReturn(destination);

        Destination result =
                destinationService.saveDestination(destination);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(destinationRepository).save(destination);
    }

    @Test
    void getAllDestinations_shouldReturnAllDestinations() {
        Destination secondDestination = new Destination();
        secondDestination.setId(2L);

        when(destinationRepository.findAll())
                .thenReturn(List.of(destination, secondDestination));

        List<Destination> result =
                destinationService.getAllDestinations();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(destinationRepository).findAll();
    }

    @Test
    void findById_shouldReturnDestinationWhenFound() {
        when(destinationRepository.findById(1L))
                .thenReturn(Optional.of(destination));

        Destination result =
                destinationService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(destinationRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        when(destinationRepository.findById(99L))
                .thenReturn(Optional.empty());

        Destination result =
                destinationService.findById(99L);

        assertNull(result);

        verify(destinationRepository).findById(99L);
    }

    @Test
    void deleteDestination_shouldDeleteById() {
        destinationService.deleteDestination(1L);

        verify(destinationRepository).deleteById(1L);
    }
}
