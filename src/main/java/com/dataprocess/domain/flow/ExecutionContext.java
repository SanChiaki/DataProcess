package com.dataprocess.domain.flow;

import com.dataprocess.domain.operation.Port;
import com.dataprocess.domain.sheet.Sheet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionContext implements AutoCloseable {
    @Getter
    private final ExecutionConfig config;
    
    // 需要清理的资源
    private final List<AutoCloseable> resources = new ArrayList<>();
    
    // 端口数据
    private final Map<Port, List<Sheet>> portData = new HashMap<>();
    
    public ExecutionContext(ExecutionConfig config) {
        this.config = config;
    }
    
    public void addResource(AutoCloseable resource) {
        resources.add(resource);
    }
    
    public List<Sheet> getPortData(Port port) {
        return portData.get(port);
    }
    
    public void setPortData(Port port, List<Sheet> data) {
        portData.put(port, data);
    }
    
    public void fail(Exception e) {
        close();
        throw new FlowExecuteException("Flow execution failed", e);
    }
    
    @Override
    public void close() {
        // 反向关闭资源
        for (int i = resources.size() - 1; i >= 0; i--) {
            try {
                resources.get(i).close();
            } catch (Exception e) {
                // 记录日志但继续关闭其他资源
            }
        }
        resources.clear();
        portData.clear();
    }
} 