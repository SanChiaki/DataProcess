package com.example.application;

import org.springframework.stereotype.Service;

@Service
public class ProcessService {
    private final ProcessEngine processEngine;
    private final ProcessDefinitionRepository repository;
    
    public void executeProcess(String processId) {
        // 1. 加载流程定义
        ProcessDefinition definition = repository.findById(processId);
        
        // 2. 构建执行器
        ProcessExecutor executor = processEngine.buildExecutor(definition);
        
        // 3. 执行流程
        executor.execute()
            .exceptionally(throwable -> {
                // 处理执行异常
                throw new ProcessExecutionException(processId, throwable);
            });
    }
} 