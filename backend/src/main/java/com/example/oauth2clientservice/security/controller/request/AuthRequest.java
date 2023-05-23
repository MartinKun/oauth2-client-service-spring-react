package com.example.oauth2clientservice.security.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {

    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email address.")
    private String email;
    @NotEmpty(message = "Password is required.")
    private String password;
}
