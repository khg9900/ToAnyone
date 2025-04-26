package com.example.toanyone.global.config;

import com.example.toanyone.global.auth.jwt.JwtFilter;
import com.example.toanyone.global.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig { // Spring Security 설정을 커스터마이징하는 클래스

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            // 2. JWT 사용을 위해 세션 사용 안함으로 설정
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 3. 인증/인가 URL 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/signup", "/auth/signin", "/auth/reissue").permitAll() // 인증 없이 가능
                .requestMatchers("/owner/**").hasAuthority("OWNER") // OWNER 권한을 가진 사용자만 접근 가능
                .anyRequest().authenticated() // 그 외는 인증 필요
            )
             // 4. 로그인 관련 설정
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
