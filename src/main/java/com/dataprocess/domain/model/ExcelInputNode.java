public abstract class Node {
    private NodeId id;
    private String name;
    private NodeType type;
    private Version version;
    private NodeConfig config;
    
    public abstract DataResult process(DataContext context);
}

// 具体节点实现
public class ExcelInputNode extends Node {
    @Override
    public DataResult process(DataContext context) {
        // 实现Excel输入逻辑
    }
}

public class MergeNode extends Node {
    @Override
    public DataResult process(DataContext context) {
        // 实现数据合并逻辑
    }
} 