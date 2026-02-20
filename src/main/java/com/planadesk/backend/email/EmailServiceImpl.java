package com.planadesk.backend.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(
            String to,
            String firstName,
            String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        // ✅ REQUIRED for Brevo (must be a verified sender)
        message.setFrom("Nikhil Sivakoti <nikhilsivakoti05@gmail.com>");

        message.setTo(to);
        message.setSubject("Reset your Planadesk password");
        message.setText(
            "Hi " + firstName + ",\n\n" +
            "We received a request to reset your password.\n\n" +
            "Reset link (valid for 15 minutes):\n" +
            resetLink + "\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "— Planadesk Security Team"
        );

        mailSender.send(message);
    }
}
