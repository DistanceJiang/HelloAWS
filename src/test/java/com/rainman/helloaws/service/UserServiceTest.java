package com.rainman.helloaws.service;

import com.rainman.helloaws.entity.User;
import com.rainman.helloaws.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试类
 */
class UserServiceTest {

    // 模拟 UserRepository
    @Mock
    private UserRepository userRepository;

    // 要测试的 UserService 实例，自动注入 Mock 对象
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // 初始化 Mockito 注解
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试 findAll 方法返回空列表的情况
     */
    @Test
    void testGetAll_Users_ReturnsEmptyList() {
        // 给 userRepository.findAll() 设置返回值为一个空列表
        when(userRepository.findAll()).thenReturn(List.of());

        // 调用被测方法
        List<User> result = userService.getAllUsers();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // 验证 userRepository.findAll() 被调用了一次
        verify(userRepository, times(1)).findAll();
    }

    /**
     * 测试 findAll 方法返回多个用户的情况
     */
    @Test
    void testGetAll_Users_ReturnsMultipleUsers() {
        // 构造模拟数据
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");

        List<User> mockUsers = Arrays.asList(user1, user2);

        // 给 userRepository.findAll() 设置返回值
        when(userRepository.findAll()).thenReturn(mockUsers);

        // 调用被测方法
        List<User> result = userService.getAllUsers();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());

        // 验证 userRepository.findAll() 被调用了一次
        verify(userRepository, times(1)).findAll();
    }
}
