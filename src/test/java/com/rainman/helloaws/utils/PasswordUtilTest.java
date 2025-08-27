package com.rainman.helloaws.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PasswordUtil的单元测试类
 */
class PasswordUtilTest {

    @Test
    void testEncode_withValidPassword_shouldReturnEncodedPassword() {
        // Given
        String password = "myPassword";
        
        // When
        String encodedPassword = PasswordUtil.encode(password);
        
        // Then
        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$")); // BCrypt hash prefix
        assertNotEquals(password, encodedPassword);
    }

    @Test
    void testEncode_withEmptyPassword_shouldReturnEncodedPassword() {
        // Given
        String password = "";
        
        // When
        String encodedPassword = PasswordUtil.encode(password);
        
        // Then
        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$"));
    }

    @Test
    void testEncode_withNullPassword_shouldHandleGracefully() {
        // Given
        String password = null;
        
        // When & Then
        assertDoesNotThrow(() -> {
            String encodedPassword = PasswordUtil.encode(password);
            assertNotNull(encodedPassword);
        });
    }

    @Test
    void testMatches_withMatchingPassword_shouldReturnTrue() {
        // Given
        String password = "myPassword";
        String encodedPassword = PasswordUtil.encode(password);
        
        // When
        boolean result = PasswordUtil.matches(password, encodedPassword);
        
        // Then
        assertTrue(result);
    }

    @Test
    void testMatches_withNonMatchingPassword_shouldReturnFalse() {
        // Given
        String password = "myPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = PasswordUtil.encode(password);
        
        // When
        boolean result = PasswordUtil.matches(wrongPassword, encodedPassword);
        
        // Then
        assertFalse(result);
    }

    @Test
    void testMatches_withEmptyPasswordAndEncodedPassword_shouldHandleGracefully() {
        // Given
        String password = "";
        String encodedPassword = PasswordUtil.encode(password);
        
        // When
        boolean result = PasswordUtil.matches(password, encodedPassword);
        
        // Then
        assertTrue(result);
    }

    @Test
    void testMatches_withNullPassword_shouldHandleGracefully() {
        // Given
        String password = null;
        String encodedPassword = PasswordUtil.encode("somePassword");
        
        // When
        boolean result = PasswordUtil.matches(password, encodedPassword);
        
        // Then
        assertFalse(result);
    }

    @Test
    void testMatches_withNullEncodedPassword_shouldHandleGracefully() {
        // Given
        String password = "myPassword";
        String encodedPassword = null;
        
        // When
        boolean result = PasswordUtil.matches(password, encodedPassword);
        
        // Then
        assertFalse(result);
    }
}
