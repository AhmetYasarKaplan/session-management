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

    // burası kullanıcı giriş yaptığında çağrılıyor !!!!!
    public void createSession(Long userId, String ipAddress, String userAgent) {
        LocalDateTime now = LocalDateTime.now();

        // postgrsqle giriş geçmişine yaz
        LoginSession loginSession = new LoginSession(null, userId, ipAddress, userAgent, now);
        loginSessionRepository.save(loginSession);

        // o anki aktif oturumu redise yaz zart zurt!!!!
        ActiveSession activeSession = new ActiveSession(userId, ipAddress, userAgent, now);
        redisTemplate.opsForValue().set(SESSION_KEY_PREFIX + userId, activeSession);
    }

    public boolean removeSession(Long userId) {
        String key = SESSION_KEY_PREFIX + userId;

        // rediste gerçekten bir session var mı?
        Boolean exists = redisTemplate.hasKey(key);
        if (exists) { // exists !=null
            redisTemplate.delete(key);
            return true;
        }

        // yoksa silme ve false döndür
        return false;
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

