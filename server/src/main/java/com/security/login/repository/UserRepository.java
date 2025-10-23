package com.security.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.login.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
}
