package com.dataprocess.domain.flow;

import lombok.Data;

@Data
public class ExecutionConfig {
    private int maxConcurrency;
    private int timeoutSeconds;
} 