package com.Final_Project.Linkedin.controllers;


import com.Final_Project.Linkedin.dto.user.userReq.UserReq;
import com.Final_Project.Linkedin.dto.user.userRes.UserRes;
import com.Final_Project.Linkedin.services.userService.UserFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testCreateUser() throws Exception {
        UserReq userRequest = new UserReq();
        userRequest.setEmail("john.doe@example.com");

        UserRes userResponse = new UserRes();
        userResponse.setId(1);
        userResponse.setEmail("john.doe@example.com");

        when(userFacade.createUser(any())).thenReturn(userResponse);

        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser
    void testGetCustomer() throws Exception {
        UserRes userResponse = new UserRes();
        userResponse.setId(1);
        userResponse.setEmail("john.doe@example.com");

        when(userFacade.getUser(anyInt())).thenReturn(userResponse);

        mockMvc.perform(get("/api/customers/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
}
