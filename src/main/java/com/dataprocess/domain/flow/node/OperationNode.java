package com.dataprocess.domain.flow.node;

import com.dataprocess.domain.flow.Node;
import com.dataprocess.domain.flow.ExecutionContext;
import com.dataprocess.domain.operation.Operation;
import com.dataprocess.domain.operation.OperationFactory;
import com.dataprocess.domain.operation.Port;
import lombok.Data;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

@Data
public class OperationNode implements Node {
    private String id;
    private String name;
    private String operationType;
    private String version;
    private List<Port> ports;
    
    private final OperationFactory operationFactory;
    private Operation operation;
    
    public OperationNode(OperationFactory operationFactory) {
        this.operationFactory = operationFactory;
    }
    
    @Override
    public Set<Port> getInputPorts() {
        return operation.getInputPorts();
    }
    
    @Override
    public Set<Port> getOutputPorts() {
        return operation.getOutputPorts();
    }
    
    @Override
    public void execute(ExecutionContext context) {
        if (operation == null) {
            operation = operationFactory.createOperation(operationType, version, id);
        }
        
        // 收集输入端口的数据
        Map<Port, List<Sheet>> inputData = new HashMap<>();
        for (Port port : operation.getInputPorts()) {
            List<Sheet> data = context.getPortData(port);
            inputData.put(port, data);
        }
        
        // 执行操作
        operation.execute(inputData);
    }
} 