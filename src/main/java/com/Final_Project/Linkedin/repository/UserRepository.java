package com.Final_Project.Linkedin.repository;

import com.Final_Project.Linkedin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.tokens t WHERE u.isVerified = false AND t.expiresAt < :now")
    List<User> findUnverifiedUsersWithExpiredTokens(LocalDateTime now);
}
