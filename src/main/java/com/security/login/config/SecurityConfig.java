package com.security.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.login.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)                         // 로그인 페이지 비활성화
            .httpBasic(Customizer.withDefaults())                               // HTTP Basic 인증 커스텀 설정
            .authorizeHttpRequests(auth -> auth
                // .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()        // 인증 필요
                .requestMatchers("/api/admin/**").hasRole("ADMIN")        // 인증 필요
                .requestMatchers("/api/private/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().denyAll())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
