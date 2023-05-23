package com.example.oauth2clientservice.security.controller;

import com.example.oauth2clientservice.exception.BusinessException;
import com.example.oauth2clientservice.exception.ResourceNotFoundException;
import com.example.oauth2clientservice.security.controller.request.AuthRequest;
import com.example.oauth2clientservice.security.controller.request.RegisterRequest;
import com.example.oauth2clientservice.security.controller.response.AuthResponse;
import com.example.oauth2clientservice.security.controller.response.MessageResponse;
import com.example.oauth2clientservice.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest req
    ) throws BusinessException {
        authenticationService.register(req);
        return ResponseEntity.ok("Registrado");
    }

    @PostMapping("/activate-account")
    public ResponseEntity<MessageResponse> activateAccount(
            HttpServletRequest req
    ) throws BusinessException, ResourceNotFoundException {
        authenticationService.activateAccount(req);
        return ResponseEntity.ok(
                new MessageResponse(200, "Usuario activado")
        );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @Valid @RequestBody AuthRequest req
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(req));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            HttpServletRequest req
    ) throws BusinessException, ResourceNotFoundException {
        return ResponseEntity.ok(authenticationService.refreshToken(req));
    }
}
