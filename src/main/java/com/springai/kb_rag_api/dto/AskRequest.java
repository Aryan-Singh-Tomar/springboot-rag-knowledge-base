package com.springai.kb_rag_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AskRequest {

    @NotBlank(message = "question is required")
    @Size(max = 2000, message = "question must be at most 2000 characters")
    private String question;

    private Long documentId;

}
