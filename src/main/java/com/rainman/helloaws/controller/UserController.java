package com.rainman.helloaws.controller;

import com.rainman.helloaws.dto.ApiResponse;
import com.rainman.helloaws.dto.user.UserCreateDTO;
import com.rainman.helloaws.dto.user.UserResponseDTO;
import com.rainman.helloaws.dto.user.UserUpdateDTO;
import com.rainman.helloaws.entity.User;
import com.rainman.helloaws.entity.mapper.UserMapper;
import com.rainman.helloaws.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    private UserMapper userMapper;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<UserResponseDTO> createUser(@RequestBody @Valid UserCreateDTO user) {
        return ApiResponse.success(userMapper.toDTO(userService.createUser(userMapper.toEntity(user))));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO user) {
        return ApiResponse.success(userMapper.toDTO(userService.updateUser(id, userMapper.toEntity(user))));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponseDTO> getUser(@PathVariable Long id) {
        return ApiResponse.success(userMapper.toDTO(userService.getUser(id).orElse(null)));
    }

    @GetMapping
    public ApiResponse<Page<UserResponseDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<User> userPage = userService.getAllUsers(PageRequest.of(page, size));
        return ApiResponse.success(userPage.map(userMapper::toDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }
}
