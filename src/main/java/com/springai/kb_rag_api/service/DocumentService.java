package com.springai.kb_rag_api.service;

import com.springai.kb_rag_api.dto.DocumentCreateRequest;
import com.springai.kb_rag_api.dto.DocumentDetailResponse;
import com.springai.kb_rag_api.dto.DocumentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DocumentService {

    DocumentResponse createDocumentAndStartIngestion(DocumentCreateRequest request);
    Page<DocumentResponse> fetchDocumentPage(Pageable pageable);

    DocumentDetailResponse fetchDocumentDetail(Long documentId);


}
