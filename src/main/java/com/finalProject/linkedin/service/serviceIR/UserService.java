package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserService {
    User save(User user);

    Optional<User> findUserByEmail(String email);

    boolean deleteById(Long id);

    User getOne(Long id);

    Page<User> findAll(Pageable pageable);

    Boolean isUserVerified(String email);

    void removeUnverifiedUsers();
}
