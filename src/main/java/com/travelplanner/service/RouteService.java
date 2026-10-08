package com.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travelplanner.entity.Route;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> getRoutesByTrip(Trip trip) {
        return routeRepository.findByTrip(trip);
    }

    public Route findById(Long id) {
        return routeRepository.findById(id).orElse(null);
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}