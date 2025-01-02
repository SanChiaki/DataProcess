package com.dataprocess.domain.operation;

import com.dataprocess.domain.common.Version;
import com.dataprocess.domain.graph.Port;
import com.dataprocess.domain.sheet.Sheet;
import com.dataprocess.domain.operation.OperationException;

import java.util.Map;
import java.util.Set;

public interface Operation {
    String getId();
    String getType();
    String getVersion();
    Set<Port> getInputPorts();
    Set<Port> getOutputPorts();
    Port getPortByName(String name);
    Sheet execute(Map<Port, Sheet> inputs) throws OperationException;
} 