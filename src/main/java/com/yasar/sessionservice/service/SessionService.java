package com.yasar.sessionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yasar.sessionservice.model.ActiveSession;
import com.yasar.sessionservice.model.LoginSession;
import com.yasar.sessionservice.repository.LoginSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final LoginSessionRepository loginSessionRepository;

    @Qualifier("activeSessionRedisTemplate")
    private final RedisTemplate<String, ActiveSession> redisTemplate;

    private static final String SESSION_KEY_PREFIX = "active::user::";

    // burası kullanıcı giriş yaptığında çağrılıyor !!!!!
    public void createSession(Long userId, String ipAddress, String userAgent) {
        log.info("Creating session for user: {}", userId);
        LocalDateTime now = LocalDateTime.now();

        // postgrsqle giriş geçmişine yaz
        LoginSession loginSession = new LoginSession(null, userId, ipAddress, userAgent, now);
        loginSessionRepository.save(loginSession);

        // o anki aktif oturumu redise yaz zart zurt!!!!
        ActiveSession activeSession = new ActiveSession(userId, ipAddress, userAgent, now);
        redisTemplate.opsForValue().set(SESSION_KEY_PREFIX + userId, activeSession);
        log.debug("Session stored in Redis for user: {}", userId);
    }

    public boolean removeSession(Long userId) {
        String key = SESSION_KEY_PREFIX + userId;

        // rediste gerçekten bir session var mı?
        Boolean exists = redisTemplate.hasKey(key);
        if (exists) { // exists !=null
            redisTemplate.delete(key);
            log.info("Session removed for user: {}", userId);
            return true;
        }

        // yoksa silme ve false döndür
        return false;
    }

    // Redis'ten aktif oturum bilgisini getir
    public ActiveSession getActiveSession(Long userId) {
        log.debug("Fetching active session for user: {}", userId);
        Object raw = redisTemplate.opsForValue().get(SESSION_KEY_PREFIX + userId);
        if (raw instanceof LinkedHashMap) {
            // manuel dönüştürme
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.convertValue(raw, ActiveSession.class);
        }
        return (ActiveSession) raw;
    }

    public List<ActiveSession> getAllActiveSessions() {
        log.debug("Fetching all active sessions");
        Set<String> keys = redisTemplate.keys("active::user::*");

        if (keys == null || keys.isEmpty()) {
            return List.of();
        }

        return keys.stream()
                .map(key -> {
                    Object raw = redisTemplate.opsForValue().get(key);
                    if (raw instanceof LinkedHashMap) {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.registerModule(new JavaTimeModule());
                        return mapper.convertValue(raw, ActiveSession.class);
                    } else {
                        return (ActiveSession) raw;
                    }
                })
                .collect(Collectors.toList());
    }
}

