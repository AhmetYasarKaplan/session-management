package com.yasar.sessionservice.controller;

import com.yasar.sessionservice.model.ActiveSession;
import com.yasar.sessionservice.model.LoginSession;
import com.yasar.sessionservice.service.SessionService;
import com.yasar.sessionservice.repository.LoginSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final LoginSessionRepository loginSessionRepository;

    // Aktif oturumu getir (Redis)
    @GetMapping("/active")
    public ResponseEntity<ActiveSession> getActiveSession(@RequestParam Long userId) {
        ActiveSession session = sessionService.getActiveSession(userId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }

    // Giriş geçmişini getir (PostgreSQL)
    @GetMapping("/history")
    public ResponseEntity<List<LoginSession>> getLoginHistory(@RequestParam Long userId) {
        List<LoginSession> history = loginSessionRepository.findByUserIdOrderByLoginAtDesc(userId);
        return ResponseEntity.ok(history);
    }
}
