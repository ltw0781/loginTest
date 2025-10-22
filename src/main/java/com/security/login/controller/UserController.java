package com.security.login.controller;

import org.springframework.http.HttpStatus;
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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    // @PostMapping("/login")
    // public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
    //     return userService.login(loginRequestDTO);
    // }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        LoginResponseDTO tokens = userService.login(request);

        Cookie refreshCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(7*24*60*60); // 7 days
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new LoginResponseDTO(tokens.getAccessToken(), null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {

        String refreshToken = null;
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("refreshToken")){
                    refreshToken = cookie.getValue();
                }
            }
        }

        if(refreshToken == null || !jwtTokenProvider.validateToken(refreshToken) ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰 입니다.");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        if(!refreshToken.equals(user.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("서버에 저장된 리프레시 토큰과 다릅니다.");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        return ResponseEntity.ok(new LoginResponseDTO(newAccessToken, null));


    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken, HttpServletResponse response) {

        String token = accessToken.replace("Bearer ", "");
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        user.setRefreshToken(null);
        userRepository.save(user);

        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(refreshCookie);


        return ResponseEntity.ok("로그아웃 성공");
    }
    
}
