package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.tokens t WHERE u.isVerified = false AND t.expiresAt < :now")
    List<User> findUnverifiedUsersWithExpiredTokens(LocalDateTime now);

    @Query("SELECT u.isVerified FROM User u WHERE u.email = :email")
    Boolean findIsVerifiedByEmail(@Param("email") String email);
}
