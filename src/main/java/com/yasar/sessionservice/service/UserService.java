package com.yasar.sessionservice.service;

import com.yasar.sessionservice.model.User;
import com.yasar.sessionservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import com.yasar.sessionservice.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;

    public User login(String username, String password, HttpServletRequest request) {
        log.info("Login attempt for user: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            log.warn("Login failed for user: {}", username);
            throw new InvalidCredentialsException("Invalid username or password");
        }

        User user = userOpt.get();

        // IP ve user agent bilgisi alınıyor
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // oturum başlatılıyor
        sessionService.createSession(user.getId(), ipAddress, userAgent);
        log.info("Session created for user: {}", username);

        return user;
    }

    public boolean logout(Long userId) {
        return sessionService.removeSession(userId);
    }
}
