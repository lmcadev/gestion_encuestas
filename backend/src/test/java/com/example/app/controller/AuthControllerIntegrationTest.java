package com.example.app.controller;

import com.example.app.dto.AuthRequest;
import com.example.app.dto.UserDto;
import com.example.app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterAndLogin() throws Exception {
        // Clean repository
        userRepository.deleteAll();
        UserDto userDto = new UserDto(null, "testuser", "password", Collections.singletonList("ROLE_USER"));
        String body = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        AuthRequest authRequest = new AuthRequest("testuser", "password");
        String authBody = objectMapper.writeValueAsString(authRequest);
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authBody))
                .andExpect(status().isOk());
    }
}