package com.Final_Project.Linkedin.services.tokenService;

import com.Final_Project.Linkedin.entity.ConfirmationToken;
import com.Final_Project.Linkedin.entity.User;
import com.Final_Project.Linkedin.utils.enums.TokenType;
import com.Final_Project.Linkedin.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public String createToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                user,
                TokenType.REGISTRATION,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)  // Токен действителен 15 минут
        );
        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    public Optional<ConfirmationToken> findByTokenAndTokenType( String token, TokenType tokenType) {
        return confirmationTokenRepository.findByTokenAndTokenType(token, tokenType);
    };

    public String createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken resetToken = new ConfirmationToken(
                token,
                user,
                TokenType.PASSWORD_RESET,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24)
        );
        confirmationTokenRepository.save(resetToken);
        return token;
    }

    public void setConfirmedAt(String token, TokenType tokenType) {
        Optional<ConfirmationToken> confirmationToken = findByTokenAndTokenType(token, tokenType);
        if (confirmationToken.isPresent()) {
            ConfirmationToken tokenEntity = confirmationToken.get();
            tokenEntity.setConfirmedAt(LocalDateTime.now());
            confirmationTokenRepository.save(tokenEntity);
        }
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
    public void deleteConfirmationToken(String token, TokenType tokenType) {
        Optional<ConfirmationToken> confirmationToken = findByTokenAndTokenType(token, tokenType);
        confirmationToken.ifPresent(confirmationTokenRepository::delete);
    }
}
