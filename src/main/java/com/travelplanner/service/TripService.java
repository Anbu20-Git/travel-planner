package com.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.repository.TripRepository;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public List<Trip> getTripsByUser(User user) {
        return tripRepository.findByUser(user);
    }

    public Trip findById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}