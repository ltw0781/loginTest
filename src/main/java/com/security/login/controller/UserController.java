package com.security.login.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.login.dto.UserRequestDTO;
import com.security.login.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody UserRequestDTO userRequestDTO) {

        userService.signUp(userRequestDTO);
        //TODO: process POST request
        
        return "회원가입 성공";
    }
    
    
}
