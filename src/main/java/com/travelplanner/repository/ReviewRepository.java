package com.travelplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelplanner.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}