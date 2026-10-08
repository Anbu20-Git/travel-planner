package com.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travelplanner.entity.Itinerary;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.ItineraryRepository;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    public ItineraryService(ItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    public Itinerary saveItinerary(Itinerary itinerary) {
        return itineraryRepository.save(itinerary);
    }

    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAll();
    }

    public List<Itinerary> getItinerariesByTrip(Trip trip) {
        return itineraryRepository.findByTrip(trip);
    }

    public Itinerary findById(Long id) {
        return itineraryRepository.findById(id).orElse(null);
    }

    public void deleteItinerary(Long id) {
        itineraryRepository.deleteById(id);
    }
}