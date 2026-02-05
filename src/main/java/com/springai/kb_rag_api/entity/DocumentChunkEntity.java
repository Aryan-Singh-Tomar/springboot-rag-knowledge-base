package com.springai.kb_rag_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "document_chunks", indexes = {
        @Index(name = "idx_chunks_document_id", columnList = "documentId")
})
@Getter
@Setter
public class DocumentChunkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Simple approach: store documentId (later relation bhi use kar sakte ho)
    @Column(nullable = false)
    private Long documentId;

    @Column(nullable = false, columnDefinition = "text")
    private String chunkText;

    private String embeddingRef;

    @Column(nullable = false)
    private Integer chunkIndex;


}
