package com.springai.kb_rag_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentCreateRequest {

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "title must be at most 200 characters")
    private String title;

    @NotBlank(message = "content is required")
    @Size(max = 200000, message = "content is too large (max 200000 chars)")
    private String content;

}
