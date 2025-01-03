package com.dataprocess.domain.flow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.concurrent.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

@Data
public class Flow {
    private String id;
    private String name;
    private String description;
    
    // 节点列表
    private List<Node> nodes;
    
    // 节点间的连接关系
    private List<Connection> connections;
    
    // 执行配置
    @JsonProperty("executionConfig")
    private ExecutionConfig config;
    
    /**
     * 执行流程
     */
    public void execute() {
        // 1. 创建执行上下文
        ExecutionContext context = new ExecutionContext(config);
        ExecutorService executor = null;
        
        try {
            // 2. 构建执行图并进行拓扑排序
            Map<String, Node> nodeMap = buildExecutionGraph();
            List<Node> sortedNodes = topologicalSort(nodeMap.values());
            
            // 3. 创建线程池
            executor = Executors.newFixedThreadPool(config.getMaxConcurrency());
            
            // 4. 执行节点
            CountDownLatch latch = new CountDownLatch(sortedNodes.size());
            Map<Node, Future<?>> futures = new ConcurrentHashMap<>();
            
            for (Node node : sortedNodes) {
                // 等待所有前置节点执行完成
                waitForDependencies(node, futures);
                
                // 提交执行任务
                Future<?> future = executor.submit(() -> {
                    try {
                        node.execute(context);
                    } finally {
                        latch.countDown();
                    }
                });
                
                futures.put(node, future);
            }
            
            // 5. 等待所有节点执行完成或超时
            if (!latch.await(config.getTimeoutSeconds(), TimeUnit.SECONDS)) {
                throw new FlowExecuteException("Flow execution timeout");
            }
            
            // 6. 检查是否有任务失败
            for (Future<?> future : futures.values()) {
                try {
                    future.get(0, TimeUnit.MILLISECONDS); // 立即获取结果
                } catch (ExecutionException e) {
                    throw new FlowExecuteException("Node execution failed", e.getCause());
                }
            }
            
        } catch (Exception e) {
            context.fail(e);
            throw new FlowExecuteException("Flow execution failed", e);
        } finally {
            if (executor != null) {
                executor.shutdownNow();
            }
            context.close();
        }
    }
    
    private void waitForDependencies(Node node, Map<Node, Future<?>> futures) {
        // 获取所有输入端口连接的上游节点
        Set<Node> dependencies = node.getInputPorts().stream()
            .flatMap(port -> port.getConnectedPorts().stream())
            .map(this::findNodeByPort)
            .collect(Collectors.toSet());
        
        // 等待所有依赖节点执行完成
        for (Node dependency : dependencies) {
            Future<?> future = futures.get(dependency);
            if (future != null) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new FlowExecuteException("Dependency execution failed", e);
                }
            }
        }
    }
    
    private List<Node> topologicalSort(Collection<Node> nodes) {
        // 1. 初始化数据结构
        Map<Node, Integer> inDegree = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        List<Node> result = new ArrayList<>();
        
        // 2. 计算每个节点的入度
        for (Node node : nodes) {
            inDegree.put(node, 0);
        }
        
        for (Node node : nodes) {
            for (Port port : node.getOutputPorts()) {
                for (Port connectedPort : port.getConnectedPorts()) {
                    Node targetNode = findNodeByPort(connectedPort);
                    inDegree.merge(targetNode, 1, Integer::sum);
                }
            }
        }
        
        // 3. 将入度为0的节点加入队列
        for (Map.Entry<Node, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        // 4. 执行拓扑排序
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            result.add(node);
            
            // 减少所有后继节点的入度
            for (Port port : node.getOutputPorts()) {
                for (Port connectedPort : port.getConnectedPorts()) {
                    Node targetNode = findNodeByPort(connectedPort);
                    int newDegree = inDegree.merge(targetNode, -1, Integer::sum);
                    if (newDegree == 0) {
                        queue.offer(targetNode);
                    }
                }
            }
        }
        
        // 5. 验证是否存在环
        if (result.size() != nodes.size()) {
            throw new FlowExecuteException("Invalid graph: cycle detected during topological sort");
        }
        
        return result;
    }
    
    /**
     * 构建执行图
     */
    private Map<String, Node> buildExecutionGraph() {
        // 1. 构建节点映射
        Map<String, Node> nodeMap = nodes.stream()
            .collect(Collectors.toMap(Node::getId, Function.identity()));
        
        // 2. 建立节点间的连接关系
        for (Connection connection : connections) {
            Node fromNode = nodeMap.get(connection.getFrom().getNodeId());
            Node toNode = nodeMap.get(connection.getTo().getNodeId());
            
            if (fromNode == null || toNode == null) {
                throw new FlowExecuteException(
                    String.format("Invalid connection: node not found, from=%s, to=%s",
                        connection.getFrom().getNodeId(),
                        connection.getTo().getNodeId())
                );
            }
            
            // 获取对应的端口
            Port sourcePort = findPort(fromNode.getOutputPorts(), connection.getFrom().getPortId());
            Port targetPort = findPort(toNode.getInputPorts(), connection.getTo().getPortId());
            
            if (sourcePort == null || targetPort == null) {
                throw new FlowExecuteException(
                    String.format("Invalid connection: port not found, from=%s:%s, to=%s:%s",
                        fromNode.getId(), connection.getFrom().getPortId(),
                        toNode.getId(), connection.getTo().getPortId())
                );
            }
            
            // 建立连接
            sourcePort.connect(targetPort);
        }
        
        // 3. 验证图是否有环
        validateAcyclic(nodeMap.values());
        
        return nodeMap;
    }
    
    private Port findPort(Set<Port> ports, String portId) {
        return ports.stream()
            .filter(p -> p.getId().equals(portId))
            .findFirst()
            .orElse(null);
    }
    
    private void validateAcyclic(Collection<Node> nodes) {
        Set<Node> visited = new HashSet<>();
        Set<Node> visiting = new HashSet<>();
        
        for (Node node : nodes) {
            if (!visited.contains(node)) {
                if (hasCycle(node, visited, visiting)) {
                    throw new FlowExecuteException("Cycle detected in flow graph");
                }
            }
        }
    }
    
    private boolean hasCycle(Node node, Set<Node> visited, Set<Node> visiting) {
        visiting.add(node);
        
        // 通过端口连接找到下游节点
        for (Port port : node.getOutputPorts()) {
            for (Port connectedPort : port.getConnectedPorts()) {
                Node nextNode = findNodeByPort(connectedPort);
                if (visiting.contains(nextNode)) {
                    return true;
                }
                if (!visited.contains(nextNode) && hasCycle(nextNode, visited, visiting)) {
                    return true;
                }
            }
        }
        
        visiting.remove(node);
        visited.add(node);
        return false;
    }
    
    private Node findNodeByPort(Port port) {
        return nodes.stream()
            .filter(node -> 
                node.getInputPorts().contains(port) || 
                node.getOutputPorts().contains(port))
            .findFirst()
            .orElseThrow(() -> new FlowExecuteException("Node not found for port: " + port.getId()));
    }
} 