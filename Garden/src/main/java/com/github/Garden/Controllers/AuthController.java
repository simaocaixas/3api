package com.github.Garden.Controllers;

import com.github.Garden.Entities.AuthRequest;
import com.github.Garden.Entities.AuthResponse;
import com.github.Garden.Entities.RegisterRequest;
import com.github.Garden.Security.JwtService;
import com.github.Garden.Services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    @GetMapping({"/login"})
    public String loginRedirect(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           RedirectAttributes redirectAttributes) {

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            redirectAttributes.addFlashAttribute("error", "Username and password are required");
            return "redirect:/register";
        }

        RegisterRequest request = new RegisterRequest(username, password);
        userService.register(request);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtService.generateToken(userDetails);

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600); // 1 hour
            response.addCookie(jwtCookie);

            return "redirect:/home";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

}
