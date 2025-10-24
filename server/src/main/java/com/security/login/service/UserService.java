package com.security.login.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.login.dto.LoginRequestDTO;
import com.security.login.dto.LoginResponseDTO;
import com.security.login.dto.UserRequestDTO;
import com.security.login.entity.User;
import com.security.login.repository.UserRepository;
import com.security.login.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        String assessToken = jwtTokenProvider.generateAccessToken(user.getUsername(),user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new LoginResponseDTO(assessToken, refreshToken);

        // return jwtTokenProvider.generateToken(user.getUsername());
    }


}
