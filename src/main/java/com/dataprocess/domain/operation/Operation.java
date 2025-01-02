package com.dataprocess.domain.operation;

import com.dataprocess.domain.sheet.Sheet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Operation {
    String getId();
    String getType();
    String getVersion();
    Set<Port> getInputPorts();
    Set<Port> getOutputPorts();
    Port getPortByName(String name);
    
    void configure(Map<String, Object> config);
    Map<String, Object> getConfig();
    
    List<Sheet> execute(Map<Port, List<Sheet>> inputs) throws OperationException;
} 
