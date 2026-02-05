package com.springai.kb_rag_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class DocumentDetailResponse {
    private Long id;
    private String title;
    private String content;
    private Instant createdAt;
}
