package com.icebank.controller;

import com.icebank.model.Account;
import com.icebank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard/{id}")
    public String showDashboard(@PathVariable Long id, Model model) {
        // Fetch the account from the database
        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));

        model.addAttribute("userAccount", account);  // Add the account object to the UI model
        return "dashboard"; // This tells Spring to look for dashboard.html
    }
}
