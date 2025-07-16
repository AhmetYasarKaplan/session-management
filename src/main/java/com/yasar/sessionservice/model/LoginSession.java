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
    // Her giriş için IP, cihaz bilgisi ve tarih bilgisi saklanır.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String ipAddress;
    private String userAgent; // cihaz bilgisi
    private LocalDateTime loginAt;
}