package com.planadesk.backend.email;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendPasswordResetEmail(
            String to,
            String firstName,
            String resetLink) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> sender = Map.of(
                "email", senderEmail,
                "name", senderName
        );

        Map<String, Object> recipient = Map.of(
                "email", to,
                "name", firstName
        );

        String htmlContent =
                "<p>Hi " + firstName + ",</p>" +
                "<p>We received a request to reset your password.</p>" +
                "<p><a href='" + resetLink + "' " +
                "style='padding:10px 15px;background:#2563eb;color:white;text-decoration:none;'>"
                + "Reset Password</a></p>" +
                "<p>This link is valid for 15 minutes.</p>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "<br><p>— Planadesk Security Team</p>";

        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", List.of(recipient));
        body.put("subject", "Reset your Planadesk password");
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}