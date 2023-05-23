package com.example.oauth2clientservice.security.jwt;

import com.example.oauth2clientservice.exception.BusinessException;
import com.example.oauth2clientservice.security.controller.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest req,
            HttpServletResponse res,
            AuthenticationException e
    ) throws IOException, ServletException {

        String errorMessage = "Token not found or invalid.";
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", errorMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(errorMap)
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        res.setContentType("application/json");
        res.setStatus(errorResponse.getStatus());
        res.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        res.getWriter().flush();
        res.getWriter().close();
    }
}