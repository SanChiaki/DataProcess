package com.dataprocess.domain.graph;

public class ProcessGraph {
    private Set<Node> nodes;
    private Set<Edge> edges;
    
    public void addNode(Node node) {
        // 添加节点逻辑
    }
    
    public void connect(Port sourcePort, Port targetPort) {
        // 连接节点逻辑
    }
    
    public List<Node> getTopologicalOrder() {
        // 拓扑排序实现
    }
} 