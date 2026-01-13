package com.icebank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup"; // Looks for templates/signup.html
    }
}