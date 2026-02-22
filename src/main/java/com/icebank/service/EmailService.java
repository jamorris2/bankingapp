package com.icebank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Value("${mailtrap.api.token}")
    private String apiToken;

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${support.email}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String MAILTRAP_URL =
            "https://send.api.mailtrap.io/api/send";

    public void sendVerificationEmail(String to, String token) {
        String link = baseUrl + "/verify?token=" + token;
        sendEmail(to, "Verify your IceBank Account",
                "Click the link to verify your account: " + link);
    }

    public void sendResetPasswordEmail(String to, String token) {
        String link = baseUrl + "/changePassword?token=" + token;
        sendEmail(to, "Reset your IceBank password",
                "Click the link to reset your password: " + link);
    }

    private void sendEmail(String to, String subject, String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        String body = """
        {
          "from": { "email": "%s" },
          "to": [{ "email": "%s" }],
          "subject": "%s",
          "text": "%s"
        }
        """.formatted(fromEmail, to, subject, text);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.exchange(
                MAILTRAP_URL,
                HttpMethod.POST,
                request,
                String.class
        );
    }
}