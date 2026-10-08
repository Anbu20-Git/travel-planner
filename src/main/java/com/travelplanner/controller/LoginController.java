package com.travelplanner.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelplanner.entity.User;
import com.travelplanner.service.UserService;

@Controller
public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(email).orElse(null);

        if (user != null &&
            passwordEncoder.matches(password, user.getPassword())) {

            session.setAttribute("loggedInUserId", user.getId());
            session.setAttribute("loggedInUserName", user.getName());
            session.setAttribute("loggedInUserEmail", user.getEmail());

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin";
            }

            return "redirect:/dashboard";
        }

        redirectAttributes.addFlashAttribute(
                "error",
                "Invalid email or password!"
        );

        return "redirect:/login";
    }
}