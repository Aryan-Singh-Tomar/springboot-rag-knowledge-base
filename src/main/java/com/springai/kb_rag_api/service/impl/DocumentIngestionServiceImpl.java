package com.springai.kb_rag_api.service.impl;

import com.springai.kb_rag_api.domain.IngestionStatus;
import com.springai.kb_rag_api.entity.DocumentChunkEntity;
import com.springai.kb_rag_api.exception.DocumentNotFoundException;
import com.springai.kb_rag_api.repository.DocumentChunkRepository;
import com.springai.kb_rag_api.repository.DocumentRepository;
import com.springai.kb_rag_api.service.DocumentIngestionService;
import com.springai.kb_rag_api.service.SpringAiChunkingService;
import com.springai.kb_rag_api.service.VectorIndexService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class DocumentIngestionServiceImpl implements DocumentIngestionService {
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final SpringAiChunkingService chunkingService;
    private final VectorIndexService vectorIndexService;



    @Async
    @Transactional
    @Override
    public void ingestDocumentAsync(Long documentId, Long ownerId) {
        var doc = documentRepository.findByIdAndOwnerId(documentId, ownerId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        try {
            doc.setIngestionStatus(IngestionStatus.PROCESSING);
            doc.setIngestionError(null);

            chunkRepository.deleteAllByDocumentId(doc.getId());

            var chunkTexts = chunkingService.splitToChunkTexts(doc.getId(), doc.getContentText());

            int idx = 0;
            for (var text : chunkTexts) {
                if (text == null || text.isBlank()) continue;

                DocumentChunkEntity chunk = new DocumentChunkEntity();
                chunk.setDocumentId(doc.getId());
                chunk.setChunkIndex(idx++);
                chunk.setChunkText(text.trim());
                chunkRepository.save(chunk);
                vectorIndexService.reindexDocumentChunks(ownerId, doc.getId());
                System.out.println("Reindex start docId=" + documentId + " ownerId=" + ownerId);
                System.out.println("Chunks count=" + chunkTexts.size());

            }


            doc.setIngestionStatus(IngestionStatus.COMPLETED);
            vectorIndexService.reindexDocumentChunks(ownerId, doc.getId());
            doc.setIngestedAt(Instant.now());

        } catch (Exception ex) {
            doc.setIngestionStatus(IngestionStatus.FAILED);
            doc.setIngestionError(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public void ingestDocumentNow(Long documentId, Long ownerId) {
        var doc = documentRepository.findByIdAndOwnerId(documentId, ownerId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        try {
            doc.setIngestionStatus(IngestionStatus.PROCESSING);
            doc.setIngestionError(null);

            chunkRepository.deleteAllByDocumentId(doc.getId());

            var chunkTexts = chunkingService.splitToChunkTexts(doc.getId(), doc.getContentText());

            int idx = 0;
            for (var text : chunkTexts) {
                if (text == null || text.isBlank()) continue;

                DocumentChunkEntity chunk = new DocumentChunkEntity();
                chunk.setDocumentId(doc.getId());
                chunk.setChunkIndex(idx++);
                chunk.setChunkText(text.trim());
                chunkRepository.save(chunk);
            }

            doc.setIngestionStatus(IngestionStatus.COMPLETED);
            doc.setIngestedAt(Instant.now());

        } catch (Exception ex) {
            doc.setIngestionStatus(IngestionStatus.FAILED);
            doc.setIngestionError(ex.getMessage());
        }
    }

}
