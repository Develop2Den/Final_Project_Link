package com.finalProject.linkedin.utils.functions;

import com.finalProject.linkedin.dto.responce.user.CreateUserRes;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
@RequiredArgsConstructor
public class UtilsFunc {

    private static UserServiceImpl userService;

    public static CreateUserRes getUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.getCurrentUser(email);
    }

//    public static String getUrl() {
//        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//    }
}
