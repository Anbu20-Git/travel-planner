package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travelplanner.entity.Expense;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.ExpenseRepository;
import com.travelplanner.service.ExpenseService;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense;
    private Trip trip;

    @BeforeEach
    void setUp() {
        expense = new Expense();
        expense.setId(1L);

        trip = new Trip();
        trip.setId(1L);
    }

    @Test
    void saveExpense_shouldSaveAndReturnExpense() {

        when(expenseRepository.save(expense))
                .thenReturn(expense);

        Expense result =
                expenseService.saveExpense(expense);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(expenseRepository).save(expense);
    }

    @Test
    void getAllExpenses_shouldReturnAllExpenses() {

        Expense secondExpense = new Expense();
        secondExpense.setId(2L);

        when(expenseRepository.findAll())
                .thenReturn(List.of(expense, secondExpense));

        List<Expense> result =
                expenseService.getAllExpenses();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(expenseRepository).findAll();
    }

    @Test
    void getExpensesByTrip_shouldReturnExpensesForTrip() {

        when(expenseRepository.findByTrip(trip))
                .thenReturn(List.of(expense));

        List<Expense> result =
                expenseService.getExpensesByTrip(trip);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(expenseRepository).findByTrip(trip);
    }

    @Test
    void findById_shouldReturnExpenseWhenFound() {

        when(expenseRepository.findById(1L))
                .thenReturn(Optional.of(expense));

        Expense result =
                expenseService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(expenseRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {

        when(expenseRepository.findById(99L))
                .thenReturn(Optional.empty());

        Expense result =
                expenseService.findById(99L);

        assertNull(result);

        verify(expenseRepository).findById(99L);
    }

    @Test
    void deleteExpense_shouldDeleteById() {

        expenseService.deleteExpense(1L);

        verify(expenseRepository).deleteById(1L);
    }
}