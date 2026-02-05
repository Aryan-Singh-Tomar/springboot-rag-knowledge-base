package com.springai.kb_rag_api.repository;

import com.springai.kb_rag_api.entity.ChatQueryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatQueryLogRepository extends JpaRepository<ChatQueryLogEntity, Long> {
}
