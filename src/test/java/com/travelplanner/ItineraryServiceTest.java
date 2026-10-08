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

import com.travelplanner.entity.Itinerary;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.ItineraryRepository;
import com.travelplanner.service.ItineraryService;

@ExtendWith(MockitoExtension.class)
class ItineraryServiceTest {

    @Mock
    private ItineraryRepository itineraryRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    private Itinerary itinerary;
    private Trip trip;

    @BeforeEach
    void setUp() {
        itinerary = new Itinerary();
        itinerary.setId(1L);

        trip = new Trip();
        trip.setId(1L);
    }

    @Test
    void saveItinerary_shouldSaveAndReturnItinerary() {

        when(itineraryRepository.save(itinerary))
                .thenReturn(itinerary);

        Itinerary result =
                itineraryService.saveItinerary(itinerary);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(itineraryRepository).save(itinerary);
    }

    @Test
    void getAllItineraries_shouldReturnAllItineraries() {

        Itinerary secondItinerary = new Itinerary();
        secondItinerary.setId(2L);

        when(itineraryRepository.findAll())
                .thenReturn(List.of(itinerary, secondItinerary));

        List<Itinerary> result =
                itineraryService.getAllItineraries();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(itineraryRepository).findAll();
    }

    @Test
    void getItinerariesByTrip_shouldReturnItinerariesForTrip() {

        when(itineraryRepository.findByTrip(trip))
                .thenReturn(List.of(itinerary));

        List<Itinerary> result =
                itineraryService.getItinerariesByTrip(trip);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(itineraryRepository).findByTrip(trip);
    }

    @Test
    void findById_shouldReturnItineraryWhenFound() {

        when(itineraryRepository.findById(1L))
                .thenReturn(Optional.of(itinerary));

        Itinerary result =
                itineraryService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(itineraryRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {

        when(itineraryRepository.findById(99L))
                .thenReturn(Optional.empty());

        Itinerary result =
                itineraryService.findById(99L);

        assertNull(result);

        verify(itineraryRepository).findById(99L);
    }

    @Test
    void deleteItinerary_shouldDeleteById() {

        itineraryService.deleteItinerary(1L);

        verify(itineraryRepository).deleteById(1L);
    }
}