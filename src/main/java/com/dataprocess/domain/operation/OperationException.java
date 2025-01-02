package com.dataprocess.domain.operation;

public class OperationException extends Exception {
    private final String operationId;
    private final FailureType type;

    public enum FailureType {
        INVALID_INPUT,
        COLUMN_MISMATCH,
        EXECUTION_ERROR
    }

    public OperationException(String operationId, FailureType type, String message) {
        super(message);
        this.operationId = operationId;
        this.type = type;
    }

    // getters...
} 