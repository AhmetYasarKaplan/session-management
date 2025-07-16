package com.yasar.sessionservice.controller;

import com.yasar.sessionservice.model.User;
import com.yasar.sessionservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username,
                                      @RequestParam String password,
                                      HttpServletRequest request) {
        User user = userService.login(username, password, request);
        return ResponseEntity.ok(user);
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
