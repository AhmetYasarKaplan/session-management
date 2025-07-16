package com.yasar.sessionservice.repository;

import com.yasar.sessionservice.model.LoginSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginSessionRepository extends JpaRepository<LoginSession, Long> {
    List<LoginSession> findByUserIdOrderByLoginAtDesc(Long userId);
    // bir kullanıcının login geçmişini zamana göre sıralı getirebileceğiz.
}
