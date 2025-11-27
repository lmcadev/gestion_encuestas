package com.example.app.service;

import com.example.app.dto.UserDto;
import com.example.app.model.User;

import java.util.List;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    String login(String username, String password);
    List<UserDto> findAll();
    UserDto findById(Long id);
    void deleteUser(Long id);
    User findByUsername(String username);
}