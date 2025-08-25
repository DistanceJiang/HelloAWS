package com.rainman.helloaws.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.rainman.helloaws.entity.User;
import com.rainman.helloaws.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserById_UserExists() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Tom");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userService.getUser(1L).orElse(null);
        assertNotNull(result);
        assertEquals("Tom", result.getName());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        User result = userService.getUser(2L).orElse(null);
        assertNull(result);
    }
}
