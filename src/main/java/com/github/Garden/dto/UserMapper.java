package com.github.Garden.dto;

import com.github.Garden.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles()
        );
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .roles(dto.roles())
                .build();
    }
}
