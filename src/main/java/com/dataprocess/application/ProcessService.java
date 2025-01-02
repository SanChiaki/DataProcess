package com.dataprocess.application;

public class ProcessService {
    private final ProcessGraphRepository graphRepository;
    private final OperationFactory operationFactory;
    
    // 从JSON创建处理图
    public ProcessGraph createFromJson(String jsonDefinition) {
        // 解析JSON
        // 使用OperationFactory创建操作节点
        // 构建图的节点和边
    }
    
    // 保存处理图
    public void saveGraph(ProcessGraph graph) {
        graphRepository.save(graph);
    }
    
    // 加载已有处理图
    public ProcessGraph loadGraph(String graphId) {
        return graphRepository.findById(graphId);
    }
} 