package com.springai.kb_rag_api.service.impl;

import com.springai.kb_rag_api.service.RetrievalService;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RetrievalServiceImpl implements RetrievalService {
    private final VectorStore vectorStore;

    @Override
    public List<Document> fetchRelevantChunks(Long ownerId, String query, int topK) {
        var req = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .filterExpression("ownerId == '" + ownerId + "'")
                .build();

        return vectorStore.similaritySearch(req);
    }
}
