package com.springai.kb_rag_api.service;

public interface VectorIndexService {
    void reindexDocumentChunks(Long ownerId, Long documentId);
    void deleteDocumentVectors(Long ownerId, Long documentId);
}
