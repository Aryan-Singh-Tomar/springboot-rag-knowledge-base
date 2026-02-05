package com.springai.kb_rag_api.mapper;

import com.springai.kb_rag_api.dto.DocumentDetailResponse;
import com.springai.kb_rag_api.dto.DocumentResponse;
import com.springai.kb_rag_api.entity.DocumentEntity;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    public DocumentResponse toResponse(DocumentEntity documentEntity) {
        return new DocumentResponse(documentEntity.getId(), documentEntity.getTitle(), documentEntity.getCreatedAt(), documentEntity.getIngestionStatus().name());
    }

    public DocumentDetailResponse toDetailResponse(DocumentEntity entity) {
        return new DocumentDetailResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContentText(),
                entity.getCreatedAt()
        );
    }
}
