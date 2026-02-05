package com.springai.kb_rag_api.service;

public interface DocumentIngestionService {
    void ingestDocumentAsync(Long documentId, Long ownerId);
    void ingestDocumentNow(Long documentId, Long ownerId);
}
