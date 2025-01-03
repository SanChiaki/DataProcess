package com.dataprocess.domain.operation;

import com.dataprocess.domain.sheet.Sheet;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Port {
    @Getter
    private final String id;
    
    @Getter
    private final PortType type;
    
    // 连接的端口
    private final Set<Port> connectedPorts = new HashSet<>();
    
    // 端口数据
    private List<Sheet> data;
    
    public enum PortType {
        INPUT,          // 单个输入
        INPUT_ARRAY,    // 多个输入
        OUTPUT          // 输出
    }
    
    public Port(String id, PortType type) {
        this.id = id;
        this.type = type;
    }
    
    public void connect(Port targetPort) {
        // 验证连接的合法性
        validateConnection(targetPort);
        
        // 建立双向连接
        this.connectedPorts.add(targetPort);
        targetPort.connectedPorts.add(this);
    }
    
    public Set<Port> getConnectedPorts() {
        return new HashSet<>(connectedPorts);
    }
    
    public void setData(List<Sheet> data) {
        this.data = data;
    }
    
    public List<Sheet> getData() {
        return data;
    }
    
    private void validateConnection(Port targetPort) {
        // 1. 验证端口类型
        if (this.type == PortType.INPUT || this.type == PortType.INPUT_ARRAY) {
            if (targetPort.type != PortType.OUTPUT) {
                throw new OperationException("Cannot connect input port to non-output port");
            }
        } else if (this.type == PortType.OUTPUT) {
            if (targetPort.type != PortType.INPUT && targetPort.type != PortType.INPUT_ARRAY) {
                throw new OperationException("Cannot connect output port to non-input port");
            }
        }
        
        // 2. 验证连接数量
        if (this.type == PortType.INPUT && !this.connectedPorts.isEmpty()) {
            throw new OperationException("Input port already connected");
        }
        
        if (targetPort.type == PortType.INPUT && !targetPort.connectedPorts.isEmpty()) {
            throw new OperationException("Target input port already connected");
        }
    }
} 