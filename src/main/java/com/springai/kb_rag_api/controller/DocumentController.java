package com.springai.kb_rag_api.controller;

import com.springai.kb_rag_api.dto.DocumentCreateRequest;
import com.springai.kb_rag_api.dto.DocumentDetailResponse;
import com.springai.kb_rag_api.dto.DocumentResponse;
import com.springai.kb_rag_api.security.CurrentUser;
import com.springai.kb_rag_api.service.DocumentIngestionService;
import com.springai.kb_rag_api.service.DocumentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/documents")
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentIngestionService documentIngestionService;

    @PostMapping
    public DocumentResponse createDocument(@Valid @RequestBody DocumentCreateRequest request){
        return documentService.createDocumentAndStartIngestion(request);
    }

    @GetMapping
    public Page<DocumentResponse> getDocumentsPage(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return documentService.fetchDocumentPage(pageable);
    }

    @GetMapping("/{id}")
    public DocumentDetailResponse getDocumentById(@PathVariable Long id) {
        return documentService.fetchDocumentDetail(id);
    }

    @PostMapping("/{id}/ingest")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void reIngest(@PathVariable Long id) {
        Long ownerId = CurrentUser.userIdOrThrow();
        documentIngestionService.ingestDocumentAsync(id, ownerId);
    }

}
