package com.icebank.controller;

import com.icebank.model.Account;
import com.icebank.model.AccountRequestDTO;
import com.icebank.service.AccountService;
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
    public String showSignupPage(Model model) {
        model.addAttribute("accountRequest", new AccountRequestDTO());
        return "signup"; // This looks for signup.html
    }

    @PostMapping("/signup")
    public String registerAccount(@ModelAttribute("accountRequest") AccountRequestDTO dto) {
        Account account = new Account();
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());

        accountService.saveAccount(account);

        // Redirect to login with a success parameter
        return "redirect:/login?success";
    }

    @PostMapping("/processLogin")
    public String handleLogin(@RequestParam("userID") String userID) {
        // Add validation logic here later
        return "redirect:/dashboard/" + userID;
    }
}