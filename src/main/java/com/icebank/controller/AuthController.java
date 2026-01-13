package com.icebank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // The "default" page
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // This looks for login.html
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup"; // This looks for signup.html
    }
}