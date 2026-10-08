package com.travelplanner.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.travelplanner.entity.Trip;
import com.travelplanner.entity.TripShare;
import com.travelplanner.repository.TripShareRepository;

@Service
public class TripShareService {

    private final TripShareRepository tripShareRepository;

    public TripShareService(TripShareRepository tripShareRepository) {
        this.tripShareRepository = tripShareRepository;
    }

    public TripShare createShare(Trip trip) {

        TripShare tripShare = new TripShare();

        tripShare.setTrip(trip);

        tripShare.setShareCode(
                UUID.randomUUID().toString()
        );

        tripShare.setPublicShare(true);

        return tripShareRepository.save(tripShare);
    }

    public Optional<TripShare> findByShareCode(String shareCode) {

        return tripShareRepository.findByShareCode(shareCode);
    }
}