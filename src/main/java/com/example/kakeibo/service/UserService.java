package com.example.kakeibo.service;

import com.example.kakeibo.dto.UserRegistrationDto;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("このメールアドレスは既に登録されています");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        user.setEnabled(true);
        user.setAccountLocked(false);

        User saved = userRepository.save(user);

        return saved;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    }
}
