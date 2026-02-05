package com.springai.kb_rag_api.controller;

import com.springai.kb_rag_api.security.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
    private final VectorStore vectorStore;

    @PostMapping
    public List<Map<String, Object>> search(@RequestBody Map<String, String> body) {
        Long ownerId = CurrentUser.userIdOrThrow();
        String query = body.getOrDefault("query", "");

        var req = SearchRequest.builder()
                .query(query)
                .topK(5)
                .filterExpression("ownerId == " + ownerId)   // isolate per user
                .build();

        return vectorStore.similaritySearch(req).stream()
                .map(d -> Map.of(
                        "text", Objects.requireNonNull(d.getText()),
                        "metadata", d.getMetadata()
                ))
                .toList();
    }
}
