package com.springai.kb_rag_api.repository;

import com.springai.kb_rag_api.entity.DocumentChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunkEntity, Long> {
    List<DocumentChunkEntity> findAllByDocumentId(Long documentId);
    void deleteAllByDocumentId(Long documentId);
    long countByDocumentId(Long documentId);

    List<DocumentChunkEntity> findAllByDocumentIdOrderByChunkIndexAsc(Long documentId);
}
