package com.example.app.service;

import com.example.app.dto.UserDto;
import com.example.app.exception.CustomException;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.security.JwtUtil;
import com.example.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio de usuarios.
 */
public class UserServiceTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtUtil = Mockito.mock(JwtUtil.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder, authenticationManager, jwtUtil);
    }

    @Test
    void testRegisterUser_success() {
        UserDto dto = new UserDto(null, "juan", "secret", Collections.singletonList("ROLE_USER"));
        when(userRepository.existsByUsername("juan")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        User saved = new User("juan", "hashed", Collections.singletonList("ROLE_USER"));
        saved.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserDto result = userService.registerUser(dto);
        assertNotNull(result.getId());
        assertEquals("juan", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_usernameExists() {
        when(userRepository.existsByUsername("juan")).thenReturn(true);
        UserDto dto = new UserDto(null, "juan", "secret", Collections.singletonList("ROLE_USER"));
        assertThrows(CustomException.class, () -> userService.registerUser(dto));
    }

    @Test
    void testLogin_success() {
        String username = "juan";
        String password = "password";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mockito.mock(org.springframework.security.core.Authentication.class));
        when(jwtUtil.generateToken(username)).thenReturn("token");
        String token = userService.login(username, password);
        assertEquals("token", token);
    }
}