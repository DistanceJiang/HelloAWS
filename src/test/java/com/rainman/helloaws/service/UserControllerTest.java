package com.rainman.helloaws.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rainman.helloaws.controller.UserController;
import com.rainman.helloaws.dto.user.UserCreateDTO;
import com.rainman.helloaws.dto.user.UserResponseDTO;
import com.rainman.helloaws.dto.user.UserUpdateDTO;
import com.rainman.helloaws.entity.User;
import com.rainman.helloaws.entity.mapper.UserMapper;
import com.rainman.helloaws.entity.mapper.UserMapperImpl;
import com.rainman.helloaws.utils.PasswordUtil;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({UserMapperImpl.class, UserControllerTest.TestSecurityConfig.class})
public class UserControllerTest {
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testCreateUser_ValidData() throws Exception {
        // Mock user data
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("John Doe");
        userCreateDTO.setEmail("john@example.com");
        userCreateDTO.setPassword("123");

        User user = userMapper.toEntity(userCreateDTO);
        user.setId(1L);

        // Mock service method
        when(userService.createUser(any(User.class))).thenReturn(user);

        // Send POST request to /users endpoint
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    public void testCreateUser_InvalidData() throws Exception {
        // Mock user data
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("John Doe");
        userCreateDTO.setPassword("123");

        User user = userMapper.toEntity(userCreateDTO);
        user.setId(1L);

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Email cannot be empty"));
    }

    @Test
    public void testUpdateUser_ValidData() throws Exception {
        // Mock user data
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName("John Doe");
        userUpdateDTO.setEmail("john@example.com");
        userUpdateDTO.setPassword("123");

        User user = userMapper.toEntity(userUpdateDTO);
        user.setId(1L);

        // Mock service method
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(user);

        // Send POST request to /users endpoint
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    public void testUpdateUser_UserNotFound() throws Exception {
        // Mock user data
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName("John Doe");
        userUpdateDTO.setEmail("john@example.com");
        userUpdateDTO.setPassword("123");

        User user = userMapper.toEntity(userUpdateDTO);
        user.setId(1L);

        // Mock service method
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(null);

        // Send POST request to /users endpoint
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(is(nullValue())));
    }

    @Test
    public void testGetUserById_UserExists() throws Exception {
        // Mock user data
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("123");

        // Mock service method
        when(userService.getUser(any(Long.class))).thenReturn(Optional.of(user));

        // Send POST request to /users endpoint
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    public void testGetUserById_UserNotFound() throws Exception {
        // Mock service method
        when(userService.getUser(any(Long.class))).thenReturn(Optional.empty());

        // Send POST request to /users endpoint
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(is(nullValue())));
    }

    @Test
    public void testGetAllUsers_Empty() throws Exception {
        // Mock service method
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(Page.empty());

        // Send POST request to /users endpoint
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalPages").value("1"))
                .andExpect(jsonPath("$.data.totalElements").value("0"));

    }

    @Test
    public void testGetAllUsers_NonEmpty() throws Exception {
        // Mock service method
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(new PageImpl<>(Instancio.ofList(User.class).size(10).create()));

        // Send POST request to /users endpoint
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalPages").value("1"))
                .andExpect(jsonPath("$.data.totalElements").value("10"));
    }


    @Test
    public void testDeleteUser() throws Exception {
        // Mock service method
        doNothing().when(userService).deleteUser(any(Long.class));

        // Send POST request to /users endpoint
        mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
