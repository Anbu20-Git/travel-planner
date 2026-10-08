package com.travelplanner.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Object userId = session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        String userName =
                (String) session.getAttribute("loggedInUserName");

        model.addAttribute("userName", userName);

        return "dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}