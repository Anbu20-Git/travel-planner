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

import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.repository.TripRepository;
import com.travelplanner.service.TripService;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    private Trip trip;
    private User user;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1L);

        user = new User();
        user.setId(1L);
    }

    @Test
    void saveTrip_shouldSaveAndReturnTrip() {

        when(tripRepository.save(trip))
                .thenReturn(trip);

        Trip result =
                tripService.saveTrip(trip);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(tripRepository).save(trip);
    }

    @Test
    void getAllTrips_shouldReturnAllTrips() {

        Trip secondTrip = new Trip();
        secondTrip.setId(2L);

        when(tripRepository.findAll())
                .thenReturn(List.of(trip, secondTrip));

        List<Trip> result =
                tripService.getAllTrips();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(tripRepository).findAll();
    }

    @Test
    void getTripsByUser_shouldReturnTripsForUser() {

        when(tripRepository.findByUser(user))
                .thenReturn(List.of(trip));

        List<Trip> result =
                tripService.getTripsByUser(user);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(tripRepository).findByUser(user);
    }

    @Test
    void findById_shouldReturnTripWhenFound() {

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(trip));

        Trip result =
                tripService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(tripRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {

        when(tripRepository.findById(99L))
                .thenReturn(Optional.empty());

        Trip result =
                tripService.findById(99L);

        assertNull(result);

        verify(tripRepository).findById(99L);
    }

    @Test
    void deleteTrip_shouldDeleteById() {

        tripService.deleteTrip(1L);

        verify(tripRepository).deleteById(1L);
    }
}