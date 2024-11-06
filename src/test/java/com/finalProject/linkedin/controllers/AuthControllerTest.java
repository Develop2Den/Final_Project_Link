package com.finalProject.linkedin.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.finalProject.linkedin.controller.AuthController;
import com.finalProject.linkedin.dto.request.user.CreateUserReq;
import com.finalProject.linkedin.entity.ConfirmationToken;
import com.finalProject.linkedin.entity.User;
import com.finalProject.linkedin.service.serviceImpl.AuthEmailServiceImpl;
import com.finalProject.linkedin.service.serviceImpl.ConfirmationTokenServiceImpl;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import com.finalProject.linkedin.utils.enums.TokenType;
import com.finalProject.linkedin.utils.password.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthEmailServiceImpl authEmailServiceImpl;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private ConfirmationTokenServiceImpl confirmationTokenServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerShouldReturnCreatedResponseWhenUserIsRegistered() {

        CreateUserReq createUserRequest = new CreateUserReq();
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword(PasswordValidator.create("Password123")); // замените на нужный конструктор

        when(userServiceImpl.findUserByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(confirmationTokenServiceImpl.createToken(any())).thenReturn("token");

        ResponseEntity<String> response = authController.register(createUserRequest);

        verify(userServiceImpl).save(any(User.class));
        verify(authEmailServiceImpl).sendConfirmationEmail(any(), any());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Користувач зареєстрований. Перевірте свою електронну пошту для підтвердження.", response.getBody());
    }

    @Test
    void confirmAccountShouldReturnSuccessMessageWhenTokenIsValid() {

        String token = "validToken";
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(null);
        confirmationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        User user = new User();
        confirmationToken.setUser(user);

        when(confirmationTokenServiceImpl.findByTokenAndTokenType(token, TokenType.REGISTRATION)).thenReturn(Optional.of(confirmationToken));

        String response = authController.confirmAccount(token);

        verify(confirmationTokenServiceImpl).setConfirmedAt(token, TokenType.REGISTRATION);
        assertEquals("Акаунт успішно підтверджено! Можете закрити сторінку!", response);
    }

//    @Test
//    void processForgotPasswordShouldReturnOkWhenUserFound() {
//
//        Dotenv dotenv = Dotenv.load();
//        String frontUrl = dotenv.get("FRONT_URL");
//        String email = "test@example.com";
//        User user = new User();
//        user.setEmail(email);
//
//        when(userServiceImpl.findUserByEmail(email)).thenReturn(Optional.of(user));
//        when(confirmationTokenServiceImpl.createPasswordResetTokenForUser(user)).thenReturn("resetToken");
//
//        ResponseEntity<String> response = authController.processForgotPassword(email);
//
//        verify(authEmailServiceImpl).sendResetEmail(email, frontUrl + "/password-reset?token=resetToken");
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Лист для скидання пароля надіслано", response.getBody());
//    }

    @Test
    void resetPasswordShouldReturnOkWhenTokenIsValid() {

        String token = "validResetToken";
        PasswordValidator password = PasswordValidator.create("Password123");
        ConfirmationToken resetToken = new ConfirmationToken();
        resetToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        User user = new User();
        user.setEmail("test@example.com");

        resetToken.setUser(user);

        when(confirmationTokenServiceImpl.findByTokenAndTokenType(token, TokenType.PASSWORD_RESET)).thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode(any())).thenReturn("encodedNewPassword");

        ResponseEntity<String> response = authController.resetPassword(token, password);

        verify(userServiceImpl).save(user);
        verify(confirmationTokenServiceImpl).findByTokenAndTokenType(token, TokenType.PASSWORD_RESET);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Пароль успішно скинуто", response.getBody());
    }

}

