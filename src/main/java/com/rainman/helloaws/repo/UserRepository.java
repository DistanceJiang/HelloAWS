package com.rainman.helloaws.repo;

import com.rainman.helloaws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}