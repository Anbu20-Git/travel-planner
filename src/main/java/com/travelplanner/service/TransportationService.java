package com.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travelplanner.entity.Transportation;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.TransportationRepository;

@Service
public class TransportationService {

    private final TransportationRepository transportationRepository;

    public TransportationService(
            TransportationRepository transportationRepository) {

        this.transportationRepository = transportationRepository;
    }

    public Transportation saveTransportation(
            Transportation transportation) {

        return transportationRepository.save(transportation);
    }

    public List<Transportation> getAllTransportation() {

        return transportationRepository.findAll();
    }

    public List<Transportation> getTransportationByTrip(
            Trip trip) {

        return transportationRepository.findByTrip(trip);
    }

    public Transportation findById(Long id) {

        return transportationRepository
                .findById(id)
                .orElse(null);
    }

    public void deleteTransportation(Long id) {

        transportationRepository.deleteById(id);
    }
}