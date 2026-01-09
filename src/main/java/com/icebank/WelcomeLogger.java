package com.icebank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WelcomeLogger implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("\n----------------------------------------------------------");
        System.out.println("  BANKING APP IS LIVE!");
        System.out.println("  URL: http://localhost:8082");
        System.out.println("----------------------------------------------------------\n");
    }
}
