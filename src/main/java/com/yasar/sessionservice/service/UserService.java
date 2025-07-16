package com.yasar.sessionservice.service;

import com.yasar.sessionservice.model.User;
import com.yasar.sessionservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;

    public User login(String username, String password, HttpServletRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOpt.get();

        // IP ve user-agent bilgisi alınıyor
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // Oturum başlatılıyor
        sessionService.createSession(user.getId(), ipAddress, userAgent);

        return user;
    }

    public boolean logout(Long userId) {
        return sessionService.removeSession(userId);
    }
}
