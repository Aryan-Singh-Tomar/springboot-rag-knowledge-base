package com.springai.kb_rag_api.service.impl;

import com.springai.kb_rag_api.dto.DocumentCreateRequest;
import com.springai.kb_rag_api.dto.DocumentDetailResponse;
import com.springai.kb_rag_api.dto.DocumentResponse;
import com.springai.kb_rag_api.entity.DocumentEntity;
import com.springai.kb_rag_api.exception.DocumentNotFoundException;
import com.springai.kb_rag_api.mapper.DocumentMapper;
import com.springai.kb_rag_api.repository.DocumentRepository;
import com.springai.kb_rag_api.security.CurrentUser;
import com.springai.kb_rag_api.service.DocumentIngestionService;
import com.springai.kb_rag_api.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final DocumentIngestionService documentIngestionService;

    private final AtomicLong idSeq = new AtomicLong(1);
    private static final int MAX_PAGE_SIZE = 50;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("createdAt", "title", "id");

    private Sort sanitizeSort(Sort sort) {
        if (sort == null || !sort.isSorted()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        for (Sort.Order order : sort) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                // fallback default if any invalid field present
                return Sort.by(Sort.Direction.DESC, "createdAt");
            }
        }
        return sort;
    }

    @Override
    public DocumentResponse createDocumentAndStartIngestion(DocumentCreateRequest request) {
         Long ownerId = CurrentUser.userIdOrThrow();
         DocumentEntity doc = new DocumentEntity();
         doc.setOwnerId(ownerId);
         doc.setTitle(request.getTitle());
         doc.setContentText(request.getContent());
         DocumentEntity saved = documentRepository.save(doc);
         documentIngestionService.ingestDocumentAsync(saved.getId(), ownerId);
         return documentMapper.toResponse(saved);

    }

    @Override
    public Page<DocumentResponse> fetchDocumentPage(Pageable pageable) {
        int safeSize = Math.min(pageable.getPageSize(), MAX_PAGE_SIZE);
        Long ownerId = CurrentUser.userIdOrThrow();
        Sort safeSort = sanitizeSort(pageable.getSort());

        Pageable safePageable = PageRequest.of(
                pageable.getPageNumber(),
                safeSize,
                safeSort
        );

        return documentRepository.findAllByOwnerId(ownerId, safePageable)
                .map(documentMapper::toResponse);
    }

    @Override
    public DocumentDetailResponse fetchDocumentDetail(Long documentId) {
        Long ownerId = CurrentUser.userIdOrThrow();
        var doc = documentRepository.findByIdAndOwnerId(documentId, ownerId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        return documentMapper.toDetailResponse(doc);

    }
}
