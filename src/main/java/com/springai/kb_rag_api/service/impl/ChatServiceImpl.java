package com.springai.kb_rag_api.service.impl;

import com.springai.kb_rag_api.dto.AskRequest;
import com.springai.kb_rag_api.dto.AskResponse;
import com.springai.kb_rag_api.dto.ChatAskResponse;
import com.springai.kb_rag_api.service.ChatService;
import com.springai.kb_rag_api.service.PromptServiceBuilder;
import com.springai.kb_rag_api.service.RetrievalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final RetrievalService retrievalService;
    private final PromptServiceBuilder promptBuilder;
    private final org.springframework.ai.chat.model.ChatModel chatModel;
    @Override
    public AskResponse generateAnswerFromKnowledgeBase(AskRequest request) {
        String answer = "Dummy answer for: " + request.getQuestion();
        return new AskResponse(answer, List.of());
    }

    @Override
    public ChatAskResponse ask(Long ownerId, String question, Integer topK) {
        int k = (topK == null || topK < 1 || topK > 10) ? 5 : topK;

        var contexts = retrievalService.fetchRelevantChunks(ownerId, question, k);

        String system = promptBuilder.buildSystemPrompt();
        String user = promptBuilder.buildUserPrompt(question, contexts);

        var response = chatModel.call(new org.springframework.ai.chat.prompt.Prompt(
                List.of(
                        new org.springframework.ai.chat.messages.SystemMessage(system),
                        new org.springframework.ai.chat.messages.UserMessage(user)
                )
        ));

        String answer = response.getResult().getOutput().getText();

        // sources
        var sources = contexts.stream().map(d -> {
            var md = d.getMetadata();
            String docId = String.valueOf(md.getOrDefault("documentId", "?"));
            String chunkIndex = String.valueOf(md.getOrDefault("chunkIndex", "?"));
            String preview = d.getText();
            if (preview.length() > 160) preview = preview.substring(0, 160) + "...";
            var s = new ChatAskResponse.SourceChunk();
            // setters if you use setters; else make constructor
            s.setDocumentId(docId);
            s.setChunkIndex(chunkIndex);
            s.setPreview(preview);
            return s;
        }).toList();

        return new ChatAskResponse(answer, sources);
    }
}
