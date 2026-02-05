package com.springai.kb_rag_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "chat_query_logs", indexes = {
        @Index(name = "idx_logs_user_created", columnList = "userId, createdAt")
})
@Getter
@Setter
public class ChatQueryLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "text")
    private String question;

    // example: "12,15,19" (simple for now)
    @Column(columnDefinition = "text")
    private String retrievedChunkIds;

    private Long latencyMs;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
