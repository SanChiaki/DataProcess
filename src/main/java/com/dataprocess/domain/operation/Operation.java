package com.dataprocess.domain.operation;

import com.dataprocess.domain.sheet.Sheet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Operation {
    /**
     * 获取操作的ID
     */
    String getId();
    
    /**
     * 获取操作的类型
     */
    String getType();
    
    /**
     * 获取操作的版本
     */
    String getVersion();
    
    /**
     * 获取输入端口集合
     */
    Set<Port> getInputPorts();
    
    /**
     * 获取输出端口集合
     */
    Set<Port> getOutputPorts();
    
    /**
     * 执行操作
     * @param inputData 输入端口到数据的映射
     */
    void execute(Map<Port, List<Sheet>> inputData);
} 
