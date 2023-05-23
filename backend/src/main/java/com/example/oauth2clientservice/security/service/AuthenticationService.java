package com.example.oauth2clientservice.security.service;

import com.example.oauth2clientservice.exception.BusinessException;
import com.example.oauth2clientservice.exception.ResourceNotFoundException;
import com.example.oauth2clientservice.security.controller.request.AuthRequest;
import com.example.oauth2clientservice.security.controller.request.RegisterRequest;
import com.example.oauth2clientservice.security.controller.response.AuthResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    void register(RegisterRequest request) throws BusinessException;
    void activateAccount(HttpServletRequest request) throws BusinessException, ResourceNotFoundException;

    AuthResponse authenticate(AuthRequest req);

    AuthResponse refreshToken(HttpServletRequest req) throws BusinessException, ResourceNotFoundException;
}
