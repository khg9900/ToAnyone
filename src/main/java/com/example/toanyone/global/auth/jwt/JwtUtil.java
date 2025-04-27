package com.example.toanyone.global.auth.jwt;

import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.global.auth.entity.Refresh;
import com.example.toanyone.global.auth.repository.RefreshRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final RefreshRepository refreshRepository;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 6 * 10 * 1000L; // 10분
    private static final long REFRESH_TOKEN_TIME = 24 * 60 * 10 * 1000L; // 24시간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String category, Long userId, String email, UserRole userRole) {
        Date date = new Date();
        long tokenTime = category.equals("access") ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("userRole", userRole)
                .claim("category", category)
                .setIssuedAt(date) // 발급일
                .setExpiration(new Date(date.getTime() + tokenTime)) // 만료일
                .signWith(key, signatureAlgorithm) // 서명
                .compact();
    }

    // 토큰 파싱
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // "Bearer " 이후만 추출
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new RuntimeException("Not Found Token");
    }

    // 토큰 만료 여부 확인
    public boolean isExpired(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .before(new Date());
    }

    // refresh 토큰 저장
    @Transactional
    public void saveRefreshToken(long userId, String token) {
        Date date = new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME);
        refreshRepository.save(new Refresh(userId, token, date.toString()));
    }

}
