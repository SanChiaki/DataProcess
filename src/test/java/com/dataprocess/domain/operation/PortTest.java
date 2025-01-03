package com.dataprocess.domain.operation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PortTest {
    
    @Test
    void testPortConnection() {
        // 创建输入和输出端口
        Port input = new Port("input", Port.PortType.INPUT);
        Port output = new Port("output", Port.PortType.OUTPUT);
        
        // 测试连接
        input.connect(output);
        
        // 验证连接关系
        assertTrue(input.getConnectedPorts().contains(output));
        assertTrue(output.getConnectedPorts().contains(input));
    }
    
    @Test
    void testInvalidConnection() {
        // 创建两个输入端口
        Port input1 = new Port("input1", Port.PortType.INPUT);
        Port input2 = new Port("input2", Port.PortType.INPUT);
        
        // 测试无效连接
        assertThrows(OperationException.class, () -> {
            input1.connect(input2);
        });
    }
    
    @Test
    void testInputArrayConnection() {
        // 创建数组输入端口和多个输出端口
        Port inputArray = new Port("inputArray", Port.PortType.INPUT_ARRAY);
        Port output1 = new Port("output1", Port.PortType.OUTPUT);
        Port output2 = new Port("output2", Port.PortType.OUTPUT);
        
        // 测试多重连接
        inputArray.connect(output1);
        inputArray.connect(output2);
        
        // 验证连接关系
        assertEquals(2, inputArray.getConnectedPorts().size());
        assertTrue(inputArray.getConnectedPorts().contains(output1));
        assertTrue(inputArray.getConnectedPorts().contains(output2));
    }
    
    @Test
    void testSingleInputConnection() {
        // 创建单一输入端口和多个输出端口
        Port input = new Port("input", Port.PortType.INPUT);
        Port output1 = new Port("output1", Port.PortType.OUTPUT);
        Port output2 = new Port("output2", Port.PortType.OUTPUT);
        
        // 连接第一个输出端口
        input.connect(output1);
        
        // 测试连接第二个输出端口时应该抛出异常
        assertThrows(OperationException.class, () -> {
            input.connect(output2);
        });
    }
} 