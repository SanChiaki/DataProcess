package com.dataprocess.domain.service;

import org.springframework.stereotype.Service;

@Service
public class DataProcessService {
    private final NodeFactory nodeFactory;
    private final ExecutionEngine executionEngine;
    
    public ExecutionResult executeFlow(DataProcessFlow flow, int concurrency) {
        flow.validate();
        ExecutionPlan plan = flow.createExecutionPlan(concurrency);
        return executionEngine.execute(plan);
    }
    
    public DataProcessFlow createFlowFromJson(String json) {
        // 从JSON创建数据处理流程
    }
} 