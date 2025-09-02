package com.rainman.helloaws.service;

import com.rainman.helloaws.dto.login.LoginRequestDTO;
import com.rainman.helloaws.dto.login.LoginResponseDTO;
import com.rainman.helloaws.dto.user.UserCreateDTO;
import com.rainman.helloaws.dto.user.UserResponseDTO;
import com.rainman.helloaws.entity.User;
import com.rainman.helloaws.entity.mapper.UserMapper;
import com.rainman.helloaws.exception.BusinessException;
import com.rainman.helloaws.repo.UserRepository;
import com.rainman.helloaws.utils.JwtUtil;
import com.rainman.helloaws.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.value(), "User not found"));

        if (!passwordUtil.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException(HttpStatus.FORBIDDEN.value(), "Invalid credentials");
        }

        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setId(user.getId());
        loginResponse.setName(user.getName());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setToken(jwtUtil.generateToken(user.getEmail()));
        return loginResponse;
    }

    public UserResponseDTO register(UserCreateDTO user) {
        return userMapper.toDTO(userService.createUser(userMapper.toEntity(user)));
    }
}
