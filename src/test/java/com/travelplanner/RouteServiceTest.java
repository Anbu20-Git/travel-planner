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

import com.travelplanner.entity.Route;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.RouteRepository;
import com.travelplanner.service.RouteService;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    private Route route;
    private Trip trip;

    @BeforeEach
    void setUp() {
        route = new Route();
        route.setId(1L);

        trip = new Trip();
        trip.setId(1L);
    }

    @Test
    void saveRoute_shouldSaveAndReturnRoute() {

        when(routeRepository.save(route))
                .thenReturn(route);

        Route result =
                routeService.saveRoute(route);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(routeRepository).save(route);
    }

    @Test
    void getRoutesByTrip_shouldReturnRoutesForTrip() {

        Route secondRoute = new Route();
        secondRoute.setId(2L);

        when(routeRepository.findByTrip(trip))
                .thenReturn(List.of(route, secondRoute));

        List<Route> result =
                routeService.getRoutesByTrip(trip);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(routeRepository).findByTrip(trip);
    }

    @Test
    void findById_shouldReturnRouteWhenFound() {

        when(routeRepository.findById(1L))
                .thenReturn(Optional.of(route));

        Route result =
                routeService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(routeRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {

        when(routeRepository.findById(99L))
                .thenReturn(Optional.empty());

        Route result =
                routeService.findById(99L);

        assertNull(result);

        verify(routeRepository).findById(99L);
    }

    @Test
    void deleteRoute_shouldDeleteById() {

        routeService.deleteRoute(1L);

        verify(routeRepository).deleteById(1L);
    }
}