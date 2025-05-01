package com.github.Garden.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginForm(
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {}
