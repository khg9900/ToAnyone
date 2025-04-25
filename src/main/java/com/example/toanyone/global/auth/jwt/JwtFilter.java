package com.example.toanyone.global.auth.jwt;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.global.auth.service.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 토큰 꺼내기
        String bearerJwt = request.getHeader("Authorization");

        // 2. 토큰 검증
        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
//            setErrorResponse(response, JwtErrorCode.TOKEN_REQUIRED);
            // ??
            filterChain.doFilter(request, response);
//            return;

        }
        // "Bearer " 이후만 추출
        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // 토큰에서 값 꺼내기
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                setErrorResponse(response, JwtErrorCode.INVALID_JWT_CLAIMS);
                return;
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", claims.get("userRole"));

            Long userId = (Long) request.getAttribute("userId");
            String email = (String) request.getAttribute("email");
            String role = (String) request.getAttribute("userRole");

            //userEntity를 생성하여 값 set
            User user = new User();
            user.setEmail(email);
            user.setPassword("temppassword");
            user.setUserRole(role);

            //UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);


//        } catch (SecurityException | MalformedJwtException e) {
//            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
//            setErrorResponse(response, JwtErrorCode.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            setErrorResponse(response, JwtErrorCode.EXPIRED_JWT_TOKEN);
//        } catch (UnsupportedJwtException e) {
//            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
//            setErrorResponse(response, JwtErrorCode.UNSUPPORTED_JWT_TOKEN);
//        } catch (Exception e) {
//            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
//            setErrorResponse(response, JwtErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public static void setErrorResponse(HttpServletResponse response, JwtErrorCode error)
        throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(error.getHttpStatus().value());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", error.getHttpStatus().name());
        errorResponse.put("code", error.getHttpStatus().value());
        errorResponse.put("message", error.getMessage());

        String s = new ObjectMapper().writeValueAsString(errorResponse);

        response.getWriter().write(s);
    }
}
