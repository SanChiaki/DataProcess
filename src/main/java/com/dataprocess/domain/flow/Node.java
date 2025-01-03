package com.dataprocess.domain.flow;

import com.dataprocess.domain.operation.Port;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SheetNode.class, name = "SHEET"),
    @JsonSubTypes.Type(value = OperationNode.class, name = "OPERATION")
})
public interface Node {
    String getId();
    
    String getName();
    
    Set<Port> getInputPorts();
    
    Set<Port> getOutputPorts();
    
    void execute(ExecutionContext context);
} 