package com.security.login.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.login.dto.UserRequestDTO;
import com.security.login.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserRequestDTO userRequestDTO) {
        if(userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자 입니다.");
        }

        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }


}
