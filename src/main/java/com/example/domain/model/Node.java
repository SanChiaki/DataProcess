public class ProcessDefinition {
    private String id;
    private String name;
    private String description;
    private String version;
    private List<Node> nodes;
    private List<Edge> edges;
    private ExecutionConfig executionConfig;
    
    // getters, setters, etc.
}

@Data
public class Node {
    private String id;
    private String type;
    private String name;
    private String version;
    private Map<String, Object> config;
}

@Data
public class Edge {
    private String id;
    private String from;
    private String to;
    private Integer order;
}

@Data
public class ExecutionConfig {
    private int maxConcurrency;
    private int timeout;
} 