package com.planadesk.backend.email;

public interface EmailService {

    void sendPasswordResetEmail(
            String to,
            String firstName,
            String resetLink
    );
}
