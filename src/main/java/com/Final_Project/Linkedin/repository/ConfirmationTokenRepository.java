package com.Final_Project.Linkedin.repository;

import com.Final_Project.Linkedin.entity.ConfirmationToken;
import com.Final_Project.Linkedin.utils.enums.TokenType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {

    Optional<ConfirmationToken> findByTokenAndTokenType(String token, TokenType tokenType);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.confirmedAt = :confirmedAt WHERE c.token = :token")
    void updateConfirmedAt(@Param("token") String token, @Param("confirmedAt") LocalDateTime confirmedAt);

}
