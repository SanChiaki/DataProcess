package org.example.domain.model.workflow;

import org.example.domain.model.operation.Operation;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

public class WorkflowNode {
    private final NodeId id;
    private final Operation operation;
    private final Map<String, String> inputMappings;
    
    public WorkflowNode(NodeId id, Operation operation) {
        this.id = id;
        this.operation = operation;
        this.inputMappings = new HashMap<>();
    }
    
    public void addInputMapping(String inputName, String sourceNodeOutput) {
        inputMappings.put(inputName, sourceNodeOutput);
    }
    
    public Operation getOperation() {
        return operation;
    }
    
    public Map<String, String> getInputMappings() {
        return Collections.unmodifiableMap(inputMappings);
    }
} 