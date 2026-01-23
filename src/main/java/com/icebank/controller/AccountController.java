package com.icebank.controller;

import com.icebank.model.Account;
import com.icebank.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showTransferPage(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("currentUserId");
        if (id == null) return "redirect:/login";

        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        model.addAttribute("userAccount", account);

        return "transfer";
    }

    @PostMapping("/processTransfer")
    public String processTransfer(@RequestParam("amount") double amount,
                                  @RequestParam("type") String type,
                                  HttpSession session) {

        Long id = (Long) session.getAttribute("currentUserId");
        if (id == null) return "redirect:/login";

        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if ("IN".equals(type)) {
            account.setBalance(account.getBalance() + amount);
        } else if ("OUT".equals(type)) {
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
            } else {
                // Optional: Handle insufficient funds (maybe redirect with error)
                return "redirect:/transfer?error=insufficient_funds";
            }
        }

        accountService.saveAccount(account);
        return "redirect:/dashboard";
    }
}
