package com.travelplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelplanner.entity.Itinerary;
import com.travelplanner.entity.Trip;

public interface ItineraryRepository
        extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByTrip(Trip trip);
}