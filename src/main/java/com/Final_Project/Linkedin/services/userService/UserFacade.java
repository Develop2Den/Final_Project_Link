package com.Final_Project.Linkedin.services.userService;

import com.Final_Project.Linkedin.dto.user.userReq.UserReq;
import com.Final_Project.Linkedin.dto.user.userRes.UserRes;
import com.Final_Project.Linkedin.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public UserRes updateUser(Integer id, UserReq userReq) {
        User user = modelMapper.map(userReq, User.class);
        user.setUserId(id);
        User updatedUser = userService.updateUser(id, new User(
                user.getEmail(),
                passwordEncoder.encode(user.getPassword())
        ));
        return updatedUser != null ? modelMapper.map(updatedUser, UserRes.class) : null;
    }

    public UserRes getUser(Integer id) {
        User user = userService.getOne(id);
        return user != null ? modelMapper.map(user, UserRes.class) : null;
    }

    public boolean deleteUser(Integer id) {
        return userService.deleteById(id);
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
