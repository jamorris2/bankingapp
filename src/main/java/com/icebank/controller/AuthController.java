package com.icebank.controller;

import com.icebank.model.Account;
import com.icebank.model.AccountRequestDTO;
import com.icebank.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // looks for login.html
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("accountRequest", new AccountRequestDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerAccount(@ModelAttribute("accountRequest") AccountRequestDTO dto) {
        Account account = new Account();
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        account.setPassword(encodedPassword);

        accountService.saveAccount(account);
        return "redirect:/login?success";
    }

    @PostMapping("/processLogin")
    public String handleLogin(@RequestParam("userID") Long userID, HttpSession session) {
        session.setAttribute("currentUserId", userID);
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}