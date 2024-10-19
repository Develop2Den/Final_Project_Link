package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.entity.ConfirmationToken;
import com.finalProject.linkedin.entity.User;
import com.finalProject.linkedin.service.serviceIR.ConfirmationTokenService;
import com.finalProject.linkedin.utils.enums.TokenType;
import com.finalProject.linkedin.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
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
    }

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
