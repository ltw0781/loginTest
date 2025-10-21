package com.security.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.login.dto.LoginRequestDTO;
import com.security.login.dto.LoginResponseDTO;
import com.security.login.dto.UserRequestDTO;
import com.security.login.entity.User;
import com.security.login.repository.UserRepository;
import com.security.login.service.UserService;
import com.security.login.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public String signup(@RequestBody UserRequestDTO userRequestDTO) {

        userService.signUp(userRequestDTO);
        //TODO: process POST request
        
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO);
    }

    @PostMapping("/refresh")
    public LoginResponseDTO refresh(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.replace("Bearer ", "");

        if(!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String username = jwtTokenProvider.getUsernameFormToken(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if(!token.equals(user.getRefreshToken())) {
            throw new RuntimeException("토큰이 일치하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return ResponseEntity.ok(new LoginResponseDTO(newAccessToken, newRefreshToken)).getBody();
    }
    
    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String accessToken) {

        String token = accessToken.replace("Bearer ", "");
        String username = jwtTokenProvider.getUsernameFormToken(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        user.setRefreshToken(null);
        userRepository.save(user);


        return "로그아웃 성공";
    }
    
}
