package org.example.domain.model.workflow;

import org.example.domain.model.operation.Operation;
import java.util.Map;
import java.util.UUID;

public class Workflow {
    private WorkflowId id;
    private String name;
    private Map<NodeId, WorkflowNode> nodes;
    private Map<NodeId, Set<NodeId>> edges;
    private WorkflowStatus status;
    
    public Workflow(String name) {
        this.id = new WorkflowId(UUID.randomUUID());
        this.name = name;
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.status = WorkflowStatus.CREATED;
    }
    
    public void addNode(Operation operation) {
        NodeId nodeId = new NodeId(UUID.randomUUID());
        nodes.put(nodeId, new WorkflowNode(nodeId, operation));
    }
    
    public void connect(NodeId from, NodeId to) {
        edges.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
}

public record WorkflowId(UUID value) {}
public record NodeId(UUID value) {}

public enum WorkflowStatus {
    CREATED, RUNNING, COMPLETED, FAILED, CANCELLED
} 