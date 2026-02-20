package com.planadesk.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planadesk.backend.email.EmailService;
import com.planadesk.backend.model.User;
import com.planadesk.backend.repository.UserRepository;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final int TOKEN_BYTES = 32;
    private static final int EXPIRY_MINUTES = 15;

    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // ---------------- REQUEST RESET ----------------
    @Override
    @Transactional
    public void requestPasswordReset(String email) {

        userRepository.findByEmail(email)
            .ifPresent(user -> {

                String rawToken = generateSecureToken();
                String hashedToken = hashToken(rawToken);

                user.setResetTokenHash(hashedToken);
                user.setResetTokenExpiry(
                    LocalDateTime.now().plusMinutes(EXPIRY_MINUTES)
                );

                userRepository.save(user);

                String resetLink =
                    "https://planadesk-f.vercel.app/reset-password?token="
                    + rawToken;

                emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    resetLink
                );
            });

        // ❗ Always silent — prevents email enumeration
    }

    // ---------------- RESET PASSWORD ----------------
    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {

        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new RuntimeException("Invalid token or password");
        }

        String hashedToken = hashToken(token);

        User user = userRepository.findAll().stream()
            .filter(u ->
                hashedToken.equals(u.getResetTokenHash()) &&
                u.getResetTokenExpiry() != null &&
                u.getResetTokenExpiry().isAfter(LocalDateTime.now())
            )
            .findFirst()
            .orElseThrow(() ->
                new RuntimeException("Invalid token or password")
            );

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetTokenHash(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

    // ---------------- HELPERS ----------------
    private String generateSecureToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed =
                digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new IllegalStateException("Token hashing failed");
        }
    }
}
