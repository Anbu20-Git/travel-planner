package com.travelplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelplanner.entity.Route;
import com.travelplanner.entity.Trip;

public interface RouteRepository
        extends JpaRepository<Route, Long> {

    List<Route> findByTrip(Trip trip);
}