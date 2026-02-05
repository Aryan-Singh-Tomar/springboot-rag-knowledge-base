package com.springai.kb_rag_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class DocumentResponse {

        private Long id;
        private String title;
        private Instant createdAt;
        private String ingestionStatus;

        // constructor + getters

}
