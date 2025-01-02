package com.dataprocess.application;

public class ExecutionService {
    private final ConcurrentGraphExecutor executor;
    
    // 执行处理图
    public ExecutionResult execute(ProcessGraph graph, int concurrency) {
        return executor.execute(graph, concurrency);
    }
    
    // 获取执行状态
    public ExecutionStatus getStatus(String executionId) {
        return executor.getStatus(executionId);
    }
    
    // 中止执行
    public void abort(String executionId) {
        executor.abort(executionId);
    }
} 