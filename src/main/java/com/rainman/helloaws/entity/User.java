package com.rainman.helloaws.entity;

import com.rainman.helloaws.utils.PasswordUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setPassword(String password) {
        this.password = PasswordUtil.encode(password);
    }

    public boolean checkPassword(String password) {
        return PasswordUtil.matches(password, this.password);
    }
}
