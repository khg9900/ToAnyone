package com.example.toanyone.global.auth.error;

import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        ErrorStatus error = ErrorStatus.OWNER_ACCESS_DENIED;

        response.setStatus(error.getHttpStatus().value());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("isSuccess", "false");
        errorResponse.put("code", error.getCode());
        errorResponse.put("message", error.getMessage());

        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(responseBody);
    }
}
