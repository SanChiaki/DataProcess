package com.dataprocess.domain.flow;

public class FlowExecuteException extends RuntimeException {
    public FlowExecuteException(String message) {
        super(message);
    }
    
    public FlowExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
} 