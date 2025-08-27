package com.rainman.helloaws.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static final String SALT = "helloaws";

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encode(String password) {
        return passwordEncoder.encode(password + SALT);
    }

    public static boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password + SALT, encodedPassword);
    }
}
