package com.springai.kb_rag_api.exception;

public class DocumentNotFoundException extends RuntimeException{

    public DocumentNotFoundException(Long id){
        super("Document not found: " + id);
    }
}
