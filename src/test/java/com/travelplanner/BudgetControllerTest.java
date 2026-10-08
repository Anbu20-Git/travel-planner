package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.controller.BudgetController;
import com.travelplanner.entity.Expense;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.ExpenseService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    @Mock
    private ExpenseService expenseService;

    @Mock
    private TripService tripService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private BudgetController budgetController;


    @Test
    void budget_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                budgetController.budget(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(expenseService);
    }


    @Test
    void budget_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                budgetController.budget(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);

        verifyNoInteractions(tripService);
        verifyNoInteractions(expenseService);
    }


    @Test
    void budget_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                budgetController.budget(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verify(tripService).findById(10L);

        verifyNoInteractions(expenseService);
    }


    @Test
    void budget_shouldRedirectToTrips_whenUserIsNotTripOwner() {

        User loggedInUser = new User();
        loggedInUser.setId(1L);

        User tripOwner = new User();
        tripOwner.setId(2L);

        Trip trip = new Trip();
        trip.setUser(tripOwner);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(loggedInUser);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                budgetController.budget(
                        10L,
                        session,
                        model
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(expenseService);
    }


    @Test
    void budget_shouldCalculateTotalsAndReturnView_whenExpensesHaveAmounts() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setBudget(
                BigDecimal.valueOf(10000)
        );

        Expense expense1 = new Expense();
        expense1.setAmount(
                BigDecimal.valueOf(2500)
        );

        Expense expense2 = new Expense();
        expense2.setAmount(
                BigDecimal.valueOf(1500)
        );

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        when(expenseService.getExpensesByTrip(trip))
                .thenReturn(
                        List.of(expense1, expense2)
                );

        String result =
                budgetController.budget(
                        10L,
                        session,
                        model
                );

        assertEquals("budget", result);

        verify(model).addAttribute(
                "trip",
                trip
        );

        verify(model).addAttribute(
                "expenses",
                List.of(expense1, expense2)
        );

        verify(model).addAttribute(
                "totalExpenses",
                BigDecimal.valueOf(4000)
        );

        verify(model).addAttribute(
                "remainingBudget",
                BigDecimal.valueOf(6000)
        );
    }


    @Test
    void budget_shouldIgnoreNullExpenseAmount() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setBudget(
                BigDecimal.valueOf(5000)
        );

        Expense expenseWithAmount = new Expense();
        expenseWithAmount.setAmount(
                BigDecimal.valueOf(1000)
        );

        Expense expenseWithoutAmount = new Expense();
        expenseWithoutAmount.setAmount(null);

        List<Expense> expenses =
                List.of(
                        expenseWithAmount,
                        expenseWithoutAmount
                );

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        when(expenseService.getExpensesByTrip(trip))
                .thenReturn(expenses);

        String result =
                budgetController.budget(
                        10L,
                        session,
                        model
                );

        assertEquals("budget", result);

        verify(model).addAttribute(
                "totalExpenses",
                BigDecimal.valueOf(1000)
        );

        verify(model).addAttribute(
                "remainingBudget",
                BigDecimal.valueOf(4000)
        );
    }


    @Test
    void createExpense_shouldRedirectToLogin_whenSessionUserIdIsNull() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(null);

        String result =
                budgetController.createExpense(
                        10L,
                        "Food",
                        "Lunch",
                        BigDecimal.valueOf(500),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verifyNoInteractions(userService);
        verifyNoInteractions(tripService);
        verifyNoInteractions(expenseService);
    }


    @Test
    void createExpense_shouldRedirectToLogin_whenUserNotFound() {

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(null);

        String result =
                budgetController.createExpense(
                        10L,
                        "Food",
                        "Lunch",
                        BigDecimal.valueOf(500),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/login", result);

        verify(userService).findById(1L);

        verifyNoInteractions(tripService);
        verifyNoInteractions(expenseService);
    }


    @Test
    void createExpense_shouldRedirectToTrips_whenTripNotFound() {

        User user = new User();
        user.setId(1L);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(null);

        String result =
                budgetController.createExpense(
                        10L,
                        "Food",
                        "Lunch",
                        BigDecimal.valueOf(500),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(expenseService);
    }


    @Test
    void createExpense_shouldRedirectToTrips_whenUserIsNotTripOwner() {

        User loggedInUser = new User();
        loggedInUser.setId(1L);

        User tripOwner = new User();
        tripOwner.setId(2L);

        Trip trip = new Trip();
        trip.setUser(tripOwner);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(loggedInUser);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                budgetController.createExpense(
                        10L,
                        "Food",
                        "Lunch",
                        BigDecimal.valueOf(500),
                        session,
                        redirectAttributes
                );

        assertEquals("redirect:/trips", result);

        verifyNoInteractions(expenseService);
    }


    @Test
    void createExpense_shouldSaveExpense_whenUserOwnsTrip() {

        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        BigDecimal amount =
                BigDecimal.valueOf(750);

        when(session.getAttribute("loggedInUserId"))
                .thenReturn(1L);

        when(userService.findById(1L))
                .thenReturn(user);

        when(tripService.findById(10L))
                .thenReturn(trip);

        String result =
                budgetController.createExpense(
                        10L,
                        "Food",
                        "Dinner",
                        amount,
                        session,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/budget?tripId=10",
                result
        );

        ArgumentCaptor<Expense> captor =
                ArgumentCaptor.forClass(Expense.class);

        verify(expenseService)
                .saveExpense(captor.capture());

        Expense savedExpense =
                captor.getValue();

        assertEquals(
                trip,
                savedExpense.getTrip()
        );

        assertEquals(
                "Food",
                savedExpense.getCategory()
        );

        assertEquals(
                "Dinner",
                savedExpense.getDescription()
        );

        assertEquals(
                amount,
                savedExpense.getAmount()
        );

        assertEquals(
                LocalDate.now(ZoneId.of("Asia/Kolkata")),
                savedExpense.getExpenseDate()
        );

        verify(redirectAttributes)
                .addFlashAttribute(
                        "success",
                        "Expense added successfully!"
                );
    }
}