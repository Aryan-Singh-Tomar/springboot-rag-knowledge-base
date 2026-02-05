package com.springai.kb_rag_api.service;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptServiceBuilder {
    public String buildSystemPrompt() {
        return """
You are a helpful backend assistant.
Answer using only the provided context. If the answer isn't in context, say you don't know.
Be concise and clear.
""";
    }

    public String buildUserPrompt(String question, List<Document> contexts) {
        StringBuilder sb = new StringBuilder();
        sb.append("Question:\n").append(question).append("\n\n");
        sb.append("Context:\n");

        int i = 1;
        for (var d : contexts) {
            var md = d.getMetadata();
            sb.append("[").append(i++).append("] ")
                    .append("(documentId=").append(md.getOrDefault("documentId","?"))
                    .append(", chunkIndex=").append(md.getOrDefault("chunkIndex","?"))
                    .append(")\n")
                    .append(d.getText())
                    .append("\n\n");
        }
        return sb.toString();
    }
}
