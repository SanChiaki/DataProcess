package com.dataprocess.domain.operation.config;

import com.dataprocess.domain.operation.OperationFactory;
import com.dataprocess.domain.operation.impl.MergeOperation;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class OperationConfig {
    private final OperationFactory operationFactory;
    
    public OperationConfig(OperationFactory operationFactory) {
        this.operationFactory = operationFactory;
    }
    
    @PostConstruct
    public void init() {
        // 注册合并操作
        operationFactory.registerOperation(
            "MERGE", "1.0",
            MergeOperation::new
        );
    }
} 