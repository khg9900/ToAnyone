package com.example.toanyone.global.auth.jwt;

import com.example.toanyone.domain.user.enums.UserRole;
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

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if (url.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");

        if (bearerJwt == null) {
            // 토큰이 없는 경우 400을 반환합니다.
            setErrorResponse(httpResponse, JwtErrorCode.TOKEN_REQUIRED);
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                setErrorResponse(httpResponse, JwtErrorCode.INVALID_JWT_CLAIMS);
                return;
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email"));
            httpRequest.setAttribute("userRole", claims.get("userRole"));

            if (url.startsWith("/owner")) {
                // 관리자 권한이 없는 경우 403을 반환합니다.
                if (!UserRole.OWNER.equals(userRole)) {
                    httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("사장님만 접근할 수 있는 기능입니다.");
                    return;
                }
                chain.doFilter(request, response);
                return;
            }

            chain.doFilter(request, response);

        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            setErrorResponse(httpResponse, JwtErrorCode.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            setErrorResponse(httpResponse, JwtErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            setErrorResponse(httpResponse, JwtErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            setErrorResponse(httpResponse, JwtErrorCode.INVALID_JWT_TOKEN);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public static void setErrorResponse(HttpServletResponse response, JwtErrorCode error) throws IOException {
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
