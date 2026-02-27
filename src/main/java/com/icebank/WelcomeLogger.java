package com.icebank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WelcomeLogger implements CommandLineRunner {

    @Value("${app.base.url}")
    private String baseUrl;

    private final Environment environment;

    public WelcomeLogger(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(String... args) {

        boolean isLocal = Arrays.asList(environment.getActiveProfiles())
                .contains("local");

        StringBuilder message = new StringBuilder("""
                
                ----------------------------------------------------------
                  BANKING APP IS LIVE!
                  URL: %s
                """.formatted(baseUrl));

        if (isLocal) {
            message.append("""
                  Swagger: %s/swagger-ui/index.html
                  H2 Console: %s/h2-console
                  SELECT * FROM ACCOUNT;
                  DELETE FROM ACCOUNT;
                """.formatted(baseUrl, baseUrl));
        }

        message.append("""
                ----------------------------------------------------------
                """);

        System.out.println(message);
    }
}
