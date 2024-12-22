package org.example.application.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowDTO {
    private final String id;
    private final String name;
    private final List<WorkflowNodeDTO> nodes;
    private final Map<String, List<String>> edges;
    private final String status;

    public WorkflowDTO(String id, String name, List<WorkflowNodeDTO> nodes,
                      Map<String, List<String>> edges, String status) {
        this.id = id;
        this.name = name;
        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
        this.edges = Collections.unmodifiableMap(new HashMap<>(edges));
        this.status = status;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public List<WorkflowNodeDTO> getNodes() { return nodes; }
    public Map<String, List<String>> getEdges() { return edges; }
    public String getStatus() { return status; }

    public static WorkflowDTO fromDomain(Workflow workflow) {
        // 转换逻辑
        return null;
    }
} 