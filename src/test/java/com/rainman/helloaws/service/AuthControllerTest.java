package com.rainman.helloaws.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainman.helloaws.config.SecurityConfig;
import com.rainman.helloaws.controller.AuthController;
import com.rainman.helloaws.dto.login.LoginRequestDTO;
import com.rainman.helloaws.dto.login.LoginResponseDTO;
import com.rainman.helloaws.dto.user.UserCreateDTO;
import com.rainman.helloaws.dto.user.UserResponseDTO;
import com.rainman.helloaws.entity.mapper.UserMapper;
import com.rainman.helloaws.entity.mapper.UserMapperImpl;
import com.rainman.helloaws.exception.BusinessException;
import com.rainman.helloaws.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({UserMapperImpl.class, JwtUtil.class, SecurityConfig.class})
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private AuthService authService;

    @Test
    public void testLogin_RegisteredUser() throws Exception {
        // given
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "password");
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setId(1L);
        loginResponse.setName("test");
        loginResponse.setEmail(loginRequest.getEmail());
        loginResponse.setToken(jwtUtil.generateToken(loginRequest.getEmail()));

        // when
        when(authService.login(loginRequest)).thenReturn(loginResponse);

        // then
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("test"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.token").value(loginResponse.getToken()));
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        // given
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "wrong password");

        // when
        when(authService.login(loginRequest)).thenThrow(new BusinessException(HttpStatus.FORBIDDEN.value(), "Invalid credentials"));

        // then
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    public void testRegister_NewUser() throws Exception {
        // given
        UserCreateDTO user = new UserCreateDTO();
        user.setName("New User");
        user.setEmail("newuser@example.com");
        user.setPassword("password");

        UserResponseDTO userResponseDTO = userMapper.toDTO(userMapper.toEntity(user));
        userResponseDTO.setId(1L);

        // when
        when(authService.register(user)).thenReturn(userResponseDTO);

        // then
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("New User"))
                .andExpect(jsonPath("$.data.email").value("newuser@example.com"));
    }

    @Test
    public void testRegister_ExistingUser() throws Exception {
        // given
        UserCreateDTO user = new UserCreateDTO();
        user.setName("Existing User");
        user.setEmail("existinguser@example.com");
        user.setPassword("password");

        UserResponseDTO userResponseDTO = userMapper.toDTO(userMapper.toEntity(user));
        userResponseDTO.setId(1L);

        // when
        when(authService.register(user)).thenThrow(new IllegalArgumentException("Email already exists"));

        // then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }
}
