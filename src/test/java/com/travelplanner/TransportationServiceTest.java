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

import com.travelplanner.entity.Transportation;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.TransportationRepository;
import com.travelplanner.service.TransportationService;

@ExtendWith(MockitoExtension.class)
class TransportationServiceTest {

    @Mock
    private TransportationRepository transportationRepository;

    @InjectMocks
    private TransportationService transportationService;

    private Transportation transportation;
    private Trip trip;

    @BeforeEach
    void setUp() {
        transportation = new Transportation();
        transportation.setId(1L);

        trip = new Trip();
        trip.setId(1L);
    }

    @Test
    void saveTransportation_shouldSaveAndReturnTransportation() {

        when(transportationRepository.save(transportation))
                .thenReturn(transportation);

        Transportation result =
                transportationService.saveTransportation(transportation);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(transportationRepository).save(transportation);
    }

    @Test
    void getAllTransportation_shouldReturnAllTransportation() {

        Transportation secondTransportation = new Transportation();
        secondTransportation.setId(2L);

        when(transportationRepository.findAll())
                .thenReturn(List.of(transportation, secondTransportation));

        List<Transportation> result =
                transportationService.getAllTransportation();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(transportationRepository).findAll();
    }

    @Test
    void getTransportationByTrip_shouldReturnTransportationForTrip() {

        when(transportationRepository.findByTrip(trip))
                .thenReturn(List.of(transportation));

        List<Transportation> result =
                transportationService.getTransportationByTrip(trip);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(transportationRepository).findByTrip(trip);
    }

    @Test
    void findById_shouldReturnTransportationWhenFound() {

        when(transportationRepository.findById(1L))
                .thenReturn(Optional.of(transportation));

        Transportation result =
                transportationService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(transportationRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {

        when(transportationRepository.findById(99L))
                .thenReturn(Optional.empty());

        Transportation result =
                transportationService.findById(99L);

        assertNull(result);

        verify(transportationRepository).findById(99L);
    }

    @Test
    void deleteTransportation_shouldDeleteById() {

        transportationService.deleteTransportation(1L);

        verify(transportationRepository).deleteById(1L);
    }
}