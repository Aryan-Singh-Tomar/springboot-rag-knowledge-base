package com.springai.kb_rag_api.repository;

import com.springai.kb_rag_api.entity.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    Page<DocumentEntity> findAllByOwnerId(Long ownerId, Pageable pageable);
    Optional<DocumentEntity> findByIdAndOwnerId(Long id, Long ownerId);

}
