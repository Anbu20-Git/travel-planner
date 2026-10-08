package com.travelplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.User;
import com.travelplanner.service.UserService;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        User savedUser = userService.registerUser(user);

        if (savedUser == null) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Email already registered!"
            );

            return "redirect:/register";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Registration successful! Please login."
        );

        return "redirect:/login";
    }
}