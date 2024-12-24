public class NodeFactory {
    private final Map<NodeType, Map<Version, NodeCreator>> nodeCreators;
    
    public Node createNode(NodeType type, Version version, NodeConfig config) {
        // 根据节点类型和版本创建对应的节点实例
    }
} 