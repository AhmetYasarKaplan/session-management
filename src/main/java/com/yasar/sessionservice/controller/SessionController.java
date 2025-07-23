package com.yasar.sessionservice.controller;

import com.yasar.sessionservice.config.JwtUtil;
import com.yasar.sessionservice.model.ActiveSession;
import com.yasar.sessionservice.model.LoginSession;
import com.yasar.sessionservice.service.SessionService;
import com.yasar.sessionservice.repository.LoginSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final JwtUtil jwtUtil;
    private final SessionService sessionService;
    private final LoginSessionRepository loginSessionRepository;

    // Aktif oturumu getir (Redis)
    @GetMapping("/active")
    public ResponseEntity<ActiveSession> getActiveSession(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        Long userId = jwtUtil.extractUserId(token);

        ActiveSession session = sessionService.getActiveSession(userId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }


    // TODO: tokeni iptal etmek lazım logout sonrası hala erişebiliyor.
    // Giriş geçmişini getir Postgresqlden
    @GetMapping("/history")
    public ResponseEntity<List<LoginSession>> getLoginHistory(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        Long userId = jwtUtil.extractUserId(token);

        List<LoginSession> history = loginSessionRepository.findByUserIdOrderByLoginAtDesc(userId);
        return ResponseEntity.ok(history);
    }

    // bütün aktif oturumları listelemek için bu. redisten alıyor :(
    @GetMapping("/all-active")
    public ResponseEntity<List<ActiveSession>> getAllActiveSessions() {
        List<ActiveSession> sessions = sessionService.getAllActiveSessions();
        return ResponseEntity.ok(sessions);
    }

}
