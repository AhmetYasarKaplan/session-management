package com.yasar.sessionservice.repository;

import com.yasar.sessionservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    // Kullanıcı adını kullanarak kullanıcıyı bulmak için/login için gerekli
}
