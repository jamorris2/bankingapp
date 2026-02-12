package com.icebank.controller;

import com.icebank.model.Account;
import com.icebank.model.AccountRequestDTO;
import com.icebank.service.AccountService;
import com.icebank.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

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

        if (accountService.findByEmail(dto.getEmail()).isPresent()) {
            return "redirect:/signup?status=email-exists";
        }

        Account account = new Account();
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        String token = UUID.randomUUID().toString();
        account.setVerificationToken(token);

        try {
            emailService.sendVerificationEmail(account.getEmail(), token);
            accountService.saveAccount(account);
            return "redirect:/login?status=account-created";
        } catch (Exception e) {
            return "redirect:/signup?status=signup-failed";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token) {
        Optional<Account> accountOpt = accountService.findByVerificationToken(token);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setVerified(true);
            account.setVerificationToken(null);
            accountService.saveAccount(account);
            return "redirect:/login?status=account-verified";
        }

        return "redirect:/login?status=verification-error";
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam("emailAddress") String userEmail) {
        Optional<Account> accountOpt = accountService.findByEmail(userEmail);
        if (!accountOpt.isPresent()) return "redirect:/login?status=reset-password-sent";

        Account account = accountOpt.get();
        String token = UUID.randomUUID().toString();
        account.setResetPasswordToken(token);

        try {
            emailService.sendResetPasswordEmail(account.getEmail(), token);
            accountService.saveAccount(account);
            return "redirect:/login?status=reset-password-sent";
        } catch (Exception e) {
            return "redirect:/forgot-password?status=error";
        }
    }

    @GetMapping("/changePassword")
    public String showChangePassword(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String handlePasswordReset(@RequestParam("token") String token,
                                      @RequestParam("password") String newPassword) {
        Optional<Account> accountOpt = accountService.findByResetPasswordToken(token);
        if (!accountOpt.isPresent()) return "redirect:/login?status=reset-password-error";

        Account account = accountOpt.get();
        try {
            account.setPassword(passwordEncoder.encode(newPassword));
            account.setResetPasswordToken(null);
            accountService.saveAccount(account);
            return "redirect:/login?status=password-change-success";
        } catch (Exception e) {
            return "redirect:/login?status=reset-password-error";
        }
    }
}