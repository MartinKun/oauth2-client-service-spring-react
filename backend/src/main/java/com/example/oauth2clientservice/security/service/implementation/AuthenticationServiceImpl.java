package com.example.oauth2clientservice.security.service.implementation;

import com.example.oauth2clientservice.email.service.EmailService;
import com.example.oauth2clientservice.exception.BusinessException;
import com.example.oauth2clientservice.email.model.Email;
import com.example.oauth2clientservice.exception.ResourceNotFoundException;
import com.example.oauth2clientservice.security.controller.request.AuthRequest;
import com.example.oauth2clientservice.security.controller.request.RegisterRequest;
import com.example.oauth2clientservice.security.controller.response.AuthResponse;
import com.example.oauth2clientservice.security.jwt.JwtProvider;
import com.example.oauth2clientservice.security.model.entity.RefreshToken;
import com.example.oauth2clientservice.security.model.entity.Role;
import com.example.oauth2clientservice.security.model.entity.User;
import com.example.oauth2clientservice.security.model.enums.AuthProvider;
import com.example.oauth2clientservice.security.model.enums.RoleEnum;
import com.example.oauth2clientservice.security.model.enums.TokenType;
import com.example.oauth2clientservice.security.repository.RefreshTokenRepository;
import com.example.oauth2clientservice.security.repository.RoleRepository;
import com.example.oauth2clientservice.security.repository.UserRepository;
import com.example.oauth2clientservice.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String CLIENT_URL = "http://localhost:5173";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final EmailService emailService;

    @Override
    public void register(RegisterRequest req) throws BusinessException {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new BusinessException("email is already in use.");

        String password = passwordEncoder.encode(req.getPassword());
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new BusinessException("role does not exist."));
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(password)
                .looked(true)
                .roles(roles)
                .provider(AuthProvider.local)
                .build();

        userRepository.save(user);

        String token = jwtProvider.generateConfirmationToken(user);

        String registrationUrl = String.format("%s/activate-account?token=%s", CLIENT_URL, token);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("registrationUrl", registrationUrl);
        attributes.put("name", req.getName());
        String htmlBody = getHtmlBody("registration-template", attributes);

        sendEmail(
                user.getEmail(),
                "Confirmation of Registration on Our Website",
                htmlBody
        );
    }

    @Override
    public void activateAccount(HttpServletRequest req)
            throws BusinessException, ResourceNotFoundException {

        final String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("Invalid token");
        }
        String confirmToken = authHeader.substring(7);

        User user = userRepository.findByEmail(
                        jwtProvider.extractUsernameFromConfirmationToken(confirmToken)
                )
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        if (!user.isLooked())
            throw new BusinessException("The user account has already been activated.");

        user.setLooked(false);
        userRepository.save(user);
    }

    @Override
    public AuthResponse authenticate(AuthRequest req) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                req.getEmail(),
                                req.getPassword()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        revokeAllRefreshTokens(user);
        saveRefreshToken(user, refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(HttpServletRequest req)
            throws BusinessException, ResourceNotFoundException {
        final String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("Invalid token");
        }

        String refreshToken = authHeader.substring(7);

        User user = userRepository.findByEmail(
                jwtProvider.extractUsernameFromRefreshToken(refreshToken)
        ).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        revokeAllRefreshTokens(user);

        String accessToken = jwtProvider.generateAccessToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllRefreshTokens(User user) {
        List<RefreshToken> validUserTokens = refreshTokenRepository
                .findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(
                token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                }
        );
        refreshTokenRepository.saveAll(validUserTokens);
    }

    public void saveRefreshToken(User user, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        refreshTokenRepository.save(token);
    }

    public String getHtmlBody(String template, Map<String, Object> attributes) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(attributes);
        return thymeleafTemplateEngine.process(template, thymeleafContext);
    }

    private void sendEmail(String recipient, String subject, String htmlBody) {
        Email email = Email.builder()
                .recipient(recipient)
                .subject(subject)
                .body(htmlBody)
                .build();

        emailService.sendEmail(email);
    }
}
