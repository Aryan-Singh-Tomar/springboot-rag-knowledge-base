package com.springai.kb_rag_api.config;

import com.springai.kb_rag_api.entity.UserEntity;
import com.springai.kb_rag_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DevUserSeeder {
    @Bean
    CommandLineRunner seedUser(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (!userRepository.existsByUsername("demo")) {
                UserEntity u = new UserEntity();
                u.setUsername("demo");
                u.setPasswordHash(encoder.encode("demo123"));
                u.setRole("ROLE_USER");
                userRepository.save(u);
            }
        };
    }
}
