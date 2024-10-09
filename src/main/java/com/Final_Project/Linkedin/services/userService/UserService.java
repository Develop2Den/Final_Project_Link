package com.Final_Project.Linkedin.services.userService;

import com.Final_Project.Linkedin.entity.User;
import com.Final_Project.Linkedin.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User customer) {
        return userRepository.save(customer);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Scheduled(cron = "0 0/15 * * * *")
    @Transactional
    public void removeUnverifiedUsers() {
        LocalDateTime now = LocalDateTime.now();
        List<User> unverifiedUsers = userRepository.findUnverifiedUsersWithExpiredTokens(now);

        if (!unverifiedUsers.isEmpty()) {
            userRepository.deleteAll(unverifiedUsers);
            System.out.println("Deleted " + unverifiedUsers.size() + " unverified users with expired tokens");
        }
    }

}
