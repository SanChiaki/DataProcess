package com.dataprocess.domain.flow;

import lombok.Data;

@Data
public class Connection {
    private PortRef from;
    private PortRef to;
    
    @Data
    public static class PortRef {
        private String nodeId;
        private String portId;
    }
} 