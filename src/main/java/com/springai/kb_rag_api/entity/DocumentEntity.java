package com.springai.kb_rag_api.entity;


import com.springai.kb_rag_api.domain.IngestionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "documents", indexes = {
@Index(name = "idx_documents_owner_created", columnList = "ownerId, createdAt")
})
@Getter
@Setter
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable = false)
    private long ownerId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String contentText;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IngestionStatus ingestionStatus = IngestionStatus.PENDING;

    @Column(columnDefinition = "text")
    private String ingestionError;

    private Instant ingestedAt;
}
