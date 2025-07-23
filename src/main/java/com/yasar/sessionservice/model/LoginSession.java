package com.yasar.sessionservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
//giriş geçmişi logu bir nevi
public class LoginSession {
    // her giriş için IP cihaz bilgisi ve tarih bilgisi saklanır.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String ipAddress;
    private String userAgent; // cihaz bilgisi
    private LocalDateTime loginAt;
}

// eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2IiwidXNlcm5hbWUiOiJ0ZXN0dXNlcjYiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc1MzI1MDA2OSwiZXhwIjoxNzUzMjUwMzY5fQ.3EezrT_-HJdOJRxci9vcirvrDWbiuGlcdL7_nor88ys