package com.security.login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    
    @GetMapping("/api/public/hello")
    public String publicHello() {
        return"public Hello World";
    }
    
    @GetMapping("/api/private/hello")
    public String privateHello() {
        return"private Hello World";
    }

}
