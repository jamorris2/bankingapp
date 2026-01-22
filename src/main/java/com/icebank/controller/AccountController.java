package com.icebank.controller;

import com.icebank.model.Account;
import com.icebank.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("currentUserId");
        if (id == null) return "redirect:/login";

        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));

        model.addAttribute("userAccount", account);
        return "dashboard";
    }


    @GetMapping("/transfer")
    public String showTransferPage(HttpSession session) {
        if (session.getAttribute("currentUserId") == null) return "redirect:/login";

        return "transfer";
    }
}
