package com.springai.kb_rag_api.service;

import com.springai.kb_rag_api.dto.AskRequest;
import com.springai.kb_rag_api.dto.AskResponse;
import com.springai.kb_rag_api.dto.ChatAskResponse;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {

    public AskResponse generateAnswerFromKnowledgeBase(AskRequest request);
    ChatAskResponse ask(Long ownerId, String question, Integer topK);
}
