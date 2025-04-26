package com.example.toanyone.global.auth.jwt;

import com.example.toanyone.global.auth.dto.AuthUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 토큰 꺼내기
        String bearerJwt = request.getHeader("Authorization");

        // 2. 토큰 확인 -> 없을 경우 LoginFilter
        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
        }

        // 3. "Bearer " 이후만 추출
        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // 4. 토큰에서 값 꺼내기
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                setErrorResponse(response, JwtErrorCode.INVALID_JWT_CLAIMS);
                return;
            }

            // 5. 토큰에서 꺼낸 회원 정보 저장
            AuthUser authUser = new AuthUser(
                Long.parseLong(claims.getSubject()),
                (String) claims.get("email"),
                (String) claims.get("userRole")
            );

            // 6. HttpRequest 에 회원 정보 객체 담기
            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", claims.get("userRole"));


            // 7. 인가를 위한 권한 정보 저장
            Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(authUser.getUserRole()));


            // 7. Spring Security 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(authUser,
                null, authorities);

            // 8. SecurityContextHolder(세션)에 토큰 담기
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // JwtFilter 끝.
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
