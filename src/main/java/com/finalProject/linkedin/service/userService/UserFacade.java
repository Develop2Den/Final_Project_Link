package com.finalProject.linkedin.service.userService;

import com.finalProject.linkedin.dto.user.userReq.UserReq;
import com.finalProject.linkedin.dto.user.userRes.UserRes;
import com.finalProject.linkedin.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFacade {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserRes createUser(UserReq userReq) {

        User user = modelMapper.map(userReq, User.class);

        if (userReq.getPassword() != null) {
            String plainPassword = userReq.getPassword().getPassword();
            user.setPassword(passwordEncoder.encode(plainPassword));
            user.setIsVerified(true);
        }

        User savedUser = userService.save(user);
        return modelMapper.map(savedUser, UserRes.class);
    }

    public UserRes updateUser(Long id, UserReq userReq) {
        User user = modelMapper.map(userReq, User.class);
        user.setUserId(id);
        User updatedUser = userService.updateUser(id, new User(
                user.getEmail(),
                passwordEncoder.encode(user.getPassword())
        ));
        return updatedUser != null ? modelMapper.map(updatedUser, UserRes.class) : null;
    }

    public UserRes getUser(Long id) {
        User user = userService.getOne(id);
        return user != null ? modelMapper.map(user, UserRes.class) : null;
    }

    public boolean deleteUser(Long id) {
        return userService.deleteById(id);
    }

    public UserRes getCurrentUser(String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return modelMapper.map(user, UserRes.class);
        } else {
            throw new UsernameNotFoundException("Customer not found with email: " + email);
        }
    }
}
