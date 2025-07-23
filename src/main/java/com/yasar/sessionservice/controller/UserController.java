package com.yasar.sessionservice.controller;

import com.yasar.sessionservice.model.User;
import com.yasar.sessionservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {

        if (user.getRole() == null) {
            user.setRole("USER"); // default rol
        }

        // kullanıcı adı daha önce alınmış mı kontrol et
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }


        // Kaydet
        // Şifreyi hash'le
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);


        // Güvenlik için şifreyi null yap
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }
}
