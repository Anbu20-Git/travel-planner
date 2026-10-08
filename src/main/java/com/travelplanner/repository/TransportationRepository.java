package com.travelplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelplanner.entity.Transportation;
import com.travelplanner.entity.Trip;

public interface TransportationRepository
        extends JpaRepository<Transportation, Long> {

    List<Transportation> findByTrip(Trip trip);
}