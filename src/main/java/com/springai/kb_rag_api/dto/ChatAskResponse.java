package com.springai.kb_rag_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ChatAskResponse {
    private String answer;
    private List<SourceChunk> sources;

    @Getter
    @Setter
    public static class SourceChunk {
        private String documentId;
        private String chunkIndex;
        private String preview; // short snippet

        // ctor + getters
    }
}
