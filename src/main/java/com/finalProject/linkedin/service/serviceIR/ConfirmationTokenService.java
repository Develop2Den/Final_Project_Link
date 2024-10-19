package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.entity.ConfirmationToken;
import com.finalProject.linkedin.entity.User;
import com.finalProject.linkedin.utils.enums.TokenType;

import java.util.Optional;

public interface ConfirmationTokenService {
    String createToken(User user);

    Optional<ConfirmationToken> findByTokenAndTokenType(String token, TokenType tokenType);

    String createPasswordResetTokenForUser(User user);

    void setConfirmedAt(String token, TokenType tokenType);

    void deleteConfirmationToken(String token, TokenType tokenType);
}
