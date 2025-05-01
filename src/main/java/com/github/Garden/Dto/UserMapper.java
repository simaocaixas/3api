package com.github.Garden.Dto;

import com.github.Garden.Entities.Tree;
import com.github.Garden.Entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setRoles(dto.roles());
        return user;
    }

    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }

        UserDTO dto = new UserDTO(entity.getUsername(), entity.getPassword(), entity.getRoles());
        return dto;
    }

}
