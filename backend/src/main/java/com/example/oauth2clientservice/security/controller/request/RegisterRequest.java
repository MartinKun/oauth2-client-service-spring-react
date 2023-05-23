package com.example.oauth2clientservice.security.controller.request;

import com.example.oauth2clientservice.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotEmpty(message = "Username is required.")
    private String name;
    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email address.")
    private String email;
    @ValidPassword
    private String password;
}
