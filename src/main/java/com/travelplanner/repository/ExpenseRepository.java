package com.travelplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelplanner.entity.Expense;
import com.travelplanner.entity.Trip;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByTrip(Trip trip);
}