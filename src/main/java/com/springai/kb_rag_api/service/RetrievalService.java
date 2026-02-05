package com.springai.kb_rag_api.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface RetrievalService {
    List<Document> fetchRelevantChunks(Long ownerId, String query, int topK);

}
