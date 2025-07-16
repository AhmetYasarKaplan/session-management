package com.yasar.sessionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yasar.sessionservice.model.ActiveSession;
import com.yasar.sessionservice.model.LoginSession;
import com.yasar.sessionservice.repository.LoginSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final LoginSessionRepository loginSessionRepository;

    @Qualifier("activeSessionRedisTemplate")
    private final RedisTemplate<String, ActiveSession> redisTemplate;

    private static final String SESSION_KEY_PREFIX = "active::user::";

    // Kullanıcı giriş yaptığında çağrılacak
    public void createSession(Long userId, String ipAddress, String userAgent) {
        LocalDateTime now = LocalDateTime.now();

        // Giriş geçmişine yaz (PostgreSQL)
        LoginSession loginSession = new LoginSession(null, userId, ipAddress, userAgent, now);
        loginSessionRepository.save(loginSession);

        // Aktif oturumu Redis'e yaz
        ActiveSession activeSession = new ActiveSession(userId, ipAddress, userAgent, now);
        redisTemplate.opsForValue().set(SESSION_KEY_PREFIX + userId, activeSession);
    }

    // Kullanıcı çıkış yaptığında çağrılacak
    public void removeSession(Long userId) {
        redisTemplate.delete(SESSION_KEY_PREFIX + userId);
    }

    // Redis'ten aktif oturum bilgisini getir
    public ActiveSession getActiveSession(Long userId) {
        Object raw = redisTemplate.opsForValue().get(SESSION_KEY_PREFIX + userId);
        if (raw instanceof LinkedHashMap) {
            // manuel dönüştürme
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.convertValue(raw, ActiveSession.class);
        }
        return (ActiveSession) raw;
    }
}

