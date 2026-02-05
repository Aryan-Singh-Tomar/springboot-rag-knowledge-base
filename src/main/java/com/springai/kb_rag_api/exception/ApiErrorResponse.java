package com.springai.kb_rag_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String path;
    private List<FieldViolation> fieldErrors;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class FieldViolation {
        private String field;
        private String message;
    }
}
