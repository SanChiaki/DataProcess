package com.dataprocess.domain.graph;

public class Port {
    private final String name;
    private final PortType type;

    public enum PortType {
        INPUT,          // 单个输入
        INPUT_ARRAY,    // 数组输入
        OUTPUT         // 输出
    }

    public Port(String name, PortType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public PortType getType() {
        return type;
    }

    public boolean isArray() {
        return type == PortType.INPUT_ARRAY;
    }
} 