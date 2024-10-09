package com.Final_Project.Linkedin.services.userService;

import com.Final_Project.Linkedin.dto.user.userReq.UserReq;
import com.Final_Project.Linkedin.dto.user.userRes.UserRes;
import com.Final_Project.Linkedin.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFacade {

   private final UserService userService;
   @Autowired
   private final PasswordEncoder passwordEncoder;
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

    public UserRes getCurrentUser(String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            User user =optionalUser.get();
            return modelMapper.map(user,UserRes.class);
        } else {
            throw new UsernameNotFoundException("Customer not found with email: " + email);
        }
    }
}
