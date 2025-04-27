package com.example.toanyone.global.auth.jwt;

import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 토큰 꺼내기
        String bearerJwt = request.getHeader("Authorization");

        // 토큰 존재 유무 확인
        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 이후만 추출
        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // 토큰에서 값 꺼내기
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                setErrorResponse(response, ErrorStatus.INVALID_JWT_CLAIMS);
                return;
            }

            // reissue -> refresh 토큰만 가능
            if (request.getRequestURI().equals("/auth/reissue") && claims.get("category").equals("access")) {
                setErrorResponse(response, ErrorStatus.INVALID_JWT_TYPE);
                return;
            }

            // 그 외 -> access 토큰만 가능
            if (!request.getRequestURI().equals("/auth/reissue") && claims.get("category").equals("refresh")) {
                setErrorResponse(response, ErrorStatus.INVALID_JWT_TYPE);
                return;
            }

            // 토큰에서 꺼낸 회원 정보 저장
            AuthUser authUser = new AuthUser(
                Long.parseLong(claims.getSubject()),
                (String) claims.get("email"),
                (String) claims.get("userRole")
            );

            // HttpRequest 에 회원 정보 객체 담기 (@Auth 사용하기 위해)
            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", claims.get("userRole"));

            // 인가를 위한 권한 정보 저장
            Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(authUser.getUserRole()));

            // Spring Security 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(authUser,
                null, authorities);

            // SecurityContextHolder(세션)에 토큰 담기
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            setErrorResponse(response, ErrorStatus.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            setErrorResponse(response, ErrorStatus.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            setErrorResponse(response, ErrorStatus.UNSUPPORTED_JWT_TOKEN);
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            setErrorResponse(response, ErrorStatus.INVALID_JWT_TOKEN);
        }
    }

    public static void setErrorResponse(HttpServletResponse response, ErrorStatus error) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(error.getHttpStatus().value());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", error.getHttpStatus());
        errorResponse.put("code", error.getCode());
        errorResponse.put("message", error.getMessage());

        // JSON 형식으로 변환
        String s = new ObjectMapper().writeValueAsString(errorResponse);

        response.getWriter().write(s);
    }
}
