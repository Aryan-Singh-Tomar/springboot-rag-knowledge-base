package com.springai.kb_rag_api.service.impl;

import com.springai.kb_rag_api.repository.DocumentChunkRepository;
import com.springai.kb_rag_api.service.VectorIndexService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VectorIndexServiceImpl implements VectorIndexService {

    private final VectorStore vectorStore;
    private final DocumentChunkRepository chunkRepository;

    public VectorIndexServiceImpl(VectorStore vectorStore, DocumentChunkRepository chunkRepository) {
        this.vectorStore = vectorStore;
        this.chunkRepository = chunkRepository;
    }

    @Override
    public void deleteDocumentVectors(Long ownerId, Long documentId) {
        // filter expression is applied on metadata (ownerId, documentId)
        vectorStore.delete("ownerId == " + ownerId + " && documentId == " + documentId);
    }

    @Override
    public void reindexDocumentChunks(Long ownerId, Long documentId) {

        deleteDocumentVectors(ownerId, documentId);

        var chunks = chunkRepository.findAllByDocumentIdOrderByChunkIndexAsc(documentId);

        List<Document> docs = chunks.stream()
                .map(ch -> new Document(
                        ch.getChunkText(),
                        Map.of(
                                "ownerId", ownerId.toString(),
                                "documentId", documentId.toString(),
                                "chunkId", ch.getId(),
                                "chunkIndex", String.valueOf(ch.getChunkIndex())
                        )
                ))
                .toList();
        vectorStore.delete("ownerId == '" + ownerId + "' && documentId == '" + documentId + "'");
        vectorStore.add(docs);
    }

}
