package com.springai.kb_rag_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatAskRequest {
    @NotBlank
    private String question;

    private Integer topK;     // optional
    private Double minScore;  // optional

}
