package com.springai.kb_rag_api.service;

import org.springframework.ai.document.Document;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpringAiChunkingService {
    private final TokenTextSplitter splitter = TokenTextSplitter.builder()
            .withChunkSize(200)           // was 800
            .withMinChunkSizeChars(50)    // was 200
            .withMinChunkLengthToEmbed(10)
            .withMaxNumChunks(10_000)
            .withKeepSeparator(true)
            .build();



    public List<String> splitToChunkTexts(Long documentId, String content) {
        String normalized = content
                .replace("\r\n", "\n")
                .replace("\u201C", "\"")
                .replace("\u201D", "\"")
                .trim();

        Document doc = new Document(content, Map.of("documentId", documentId));

        return splitter.apply(List.of(new Document(normalized, Map.of("documentId", documentId))))
                .stream().map(Document::getText).toList();
    }

}
