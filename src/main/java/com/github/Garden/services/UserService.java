package com.github.Garden.services;

import com.github.Garden.exceptions.EmailAlreadyRegisteredException;
import com.github.Garden.forms.RegisterForm;
import com.github.Garden.entities.User;
import com.github.Garden.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User register(RegisterForm request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException("Email is already in use.");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPassword(encoder.encode(request.password()));
        user.setRoles("USER");
        userRepository.save(user);

        return user;
    }
}
