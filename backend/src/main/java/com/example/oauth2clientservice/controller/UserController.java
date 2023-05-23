package com.example.oauth2clientservice.controller;

import com.example.oauth2clientservice.controller.response.UserResponse;
import com.example.oauth2clientservice.exception.ResourceNotFoundException;
import com.example.oauth2clientservice.security.CurrentUser;
import com.example.oauth2clientservice.security.model.entity.User;
import com.example.oauth2clientservice.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @CurrentUser User user
    ) throws ResourceNotFoundException {
        User userDB = userRepository.findByEmail(user.getEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User with specified ID does not exist.")
                );
        return ResponseEntity.ok(
                UserResponse.builder()
                        .id(userDB.getId())
                        .name(user.getName())
                        .email(userDB.getEmail())
                        .imageUrl(userDB.getImageUrl())
                        .build()
        );
    }

}

