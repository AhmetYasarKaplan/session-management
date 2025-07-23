package com.yasar.sessionservice.controller;

import com.yasar.sessionservice.model.User;
import com.yasar.sessionservice.model.LoginResponse;
import com.yasar.sessionservice.service.UserService;
import com.yasar.sessionservice.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // LOGIN
    // TODO : logout olmayan kullanıcılar için login işlemi yapılmamalı
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String username,
                                               @RequestParam String password,
                                               HttpServletRequest request) {
        User user = userService.login(username, password, request);
        // şifre bilgisini dışarı vermiyoruz yoksa gözüküyordu!!!!
        user.setPassword(null);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return ResponseEntity.ok(new LoginResponse(token, user));
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        Long userId = jwtUtil.extractUserId(token);

        boolean result = userService.logout(userId);

        if (result) {
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            return ResponseEntity.status(404).body("No active session found for user.");
        }
    }

    /*
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam Long userId) {
        boolean result = userService.logout(userId);
        if (result) {
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            return ResponseEntity.status(404).body("No active session found for user.");
        }
    }
    */
}
