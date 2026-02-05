package com.springai.kb_rag_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_user", columnList = "userId"),
        @Index(name = "idx_refresh_jti", columnList = "jti", unique = true)
})
@Getter
@Setter
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 80, unique = true)
    private String jti; // token id (unique)

    @Column(nullable = false)
    private String tokenHash; // store hash, not raw token

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
