package com.finalProject.linkedin.controllers;


import com.finalProject.linkedin.controller.UserController;
import com.finalProject.linkedin.dto.request.user.CreateUserReq;
import com.finalProject.linkedin.dto.responce.user.CreateUserRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testCreateUser() throws Exception {
        CreateUserReq createUserRequest = new CreateUserReq();
        createUserRequest.setEmail("john.doe@example.com");

        CreateUserRes createUserResponse = new CreateUserRes();
        createUserResponse.setId(1L);
        createUserResponse.setEmail("john.doe@example.com");

        when(userService.createUser(any())).thenReturn(createUserResponse);

        mockMvc.perform(post("/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser
    void testGetCustomer() throws Exception {
        CreateUserRes createUserResponse = new CreateUserRes();
        createUserResponse.setId(1L);
        createUserResponse.setEmail("john.doe@example.com");
        createUserResponse.setIsVerified(true);

        when(userService.getUser(anyLong())).thenReturn(createUserResponse);

        mockMvc.perform(get("/user/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.isVerified").value(true));
    }
}
