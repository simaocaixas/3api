package com.github.Garden.Entities;

import lombok.*;

@Data
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;

}