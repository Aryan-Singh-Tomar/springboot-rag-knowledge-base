package com.springai.kb_rag_api.controller;

import com.springai.kb_rag_api.dto.ChatAskRequest;
import com.springai.kb_rag_api.dto.ChatAskResponse;
import com.springai.kb_rag_api.security.CurrentUser;
import com.springai.kb_rag_api.service.ChatService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/ask")
    public ChatAskResponse ask(@Valid @RequestBody ChatAskRequest request){
        Long ownerId = CurrentUser.userIdOrThrow();
        return chatService.ask(ownerId, request.getQuestion(), request.getTopK());
    }

}
