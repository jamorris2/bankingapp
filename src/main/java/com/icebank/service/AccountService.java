package com.icebank.service;

import com.icebank.exception.UserAlreadyExistsException;
import com.icebank.model.Account;
import com.icebank.model.AccountRequestDTO;
import com.icebank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public Account registerAccount(AccountRequestDTO dto) {
        if (accountRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        Account account = new Account();
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        String token = UUID.randomUUID().toString();
        account.setVerificationToken(token);

        Account newAccount = accountRepository.save(account);
        emailService.sendVerificationEmail(newAccount.getEmail(), newAccount.getVerificationToken());
        log.info("User successfully registered: {}", newAccount.getEmail());
        return newAccount;
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, AccountRequestDTO updatedAccount) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setName(updatedAccount.getName());
                    account.setEmail(updatedAccount.getEmail());
                    account.setBalance(updatedAccount.getBalance());
                    return accountRepository.save(account);
                })
                .orElse(null);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Optional<Account> findByVerificationToken(String token) {
        return accountRepository.findByVerificationToken(token);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .disabled(!account.isVerified()) // This prevents unverified users from logging in
                .roles("USER")
                .build();
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findByResetPasswordToken(String token) {
        return accountRepository.findByResetPasswordToken(token);
    }
}