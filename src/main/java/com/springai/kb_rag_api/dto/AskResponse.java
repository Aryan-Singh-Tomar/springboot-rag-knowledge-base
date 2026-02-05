package com.springai.kb_rag_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AskResponse {

    private String answer;
    private List<Long> usedChunkIds;
}
