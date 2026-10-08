package com.travelplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelplanner.entity.TripShare;

public interface TripShareRepository extends JpaRepository<TripShare, Long> {

    Optional<TripShare> findByShareCode(String shareCode);
}