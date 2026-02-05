package com.springai.kb_rag_api.service;

import com.springai.kb_rag_api.dto.LoginRequest;
import com.springai.kb_rag_api.dto.RefreshRequest;
import com.springai.kb_rag_api.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    TokenResponse refresh(RefreshRequest request);

    void logout(RefreshRequest request);
}
