package com.finalProject.linkedin.service.serviceIR;

public interface AuthEmailService {
    void sendConfirmationEmail(String to, String token);

    void sendResetEmail(String to, String token);
}
