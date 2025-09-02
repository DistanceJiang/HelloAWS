package com.rainman.helloaws.controller;

import com.rainman.helloaws.dto.ApiResponse;
import com.rainman.helloaws.dto.login.LoginRequestDTO;
import com.rainman.helloaws.dto.login.LoginResponseDTO;
import com.rainman.helloaws.dto.user.UserCreateDTO;
import com.rainman.helloaws.dto.user.UserResponseDTO;
import com.rainman.helloaws.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return ApiResponse.success(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> register(@RequestBody UserCreateDTO user) {
        return ApiResponse.success(authService.register(user));
    }
}
