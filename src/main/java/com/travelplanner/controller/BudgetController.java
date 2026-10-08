package com.travelplanner.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.Expense;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.service.ExpenseService;
import com.travelplanner.service.TripService;
import com.travelplanner.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BudgetController {

    private final ExpenseService expenseService;
    private final TripService tripService;
    private final UserService userService;

    public BudgetController(
            ExpenseService expenseService,
            TripService tripService,
            UserService userService) {

        this.expenseService = expenseService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/budget")
    public String budget(
            @RequestParam Long tripId,
            HttpSession session,
            Model model) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user =
                userService.findById((Long) userId);

        if (user == null) {
            return "redirect:/login";
        }

        Trip trip =
                tripService.findById(tripId);

        if (trip == null) {
            return "redirect:/trips";
        }

        // Security check:
        // Only the owner of the trip can view its budget.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        // Load only expenses belonging to this trip
        List<Expense> expenses =
                expenseService.getExpensesByTrip(trip);

        BigDecimal totalExpenses =
                BigDecimal.ZERO;

        for (Expense expense : expenses) {

            if (expense.getAmount() != null) {
                totalExpenses =
                        totalExpenses.add(
                                expense.getAmount()
                        );
            }
        }

        BigDecimal remainingBudget =
                trip.getBudget().subtract(totalExpenses);

        model.addAttribute("trip", trip);
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("remainingBudget", remainingBudget);

        return "budget";
    }

    @PostMapping("/budget/create")
    public String createExpense(
            @RequestParam Long tripId,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam BigDecimal amount,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object userId =
                session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user =
                userService.findById((Long) userId);

        if (user == null) {
            return "redirect:/login";
        }

        Trip trip =
                tripService.findById(tripId);

        if (trip == null) {
            return "redirect:/trips";
        }

        // Security check:
        // Only the owner of the trip can add expenses.
        if (!trip.getUser().getId().equals((Long) userId)) {
            return "redirect:/trips";
        }

        Expense expense = new Expense();

        expense.setTrip(trip);
        expense.setCategory(category);
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setExpenseDate(LocalDate.now());

        expenseService.saveExpense(expense);

        redirectAttributes.addFlashAttribute(
                "success",
                "Expense added successfully!"
        );

        return "redirect:/budget?tripId=" + tripId;
    }
}