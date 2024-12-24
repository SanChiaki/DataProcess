public class DataProcessFlow {
    private FlowId id;
    private String name;
    private String description;
    private List<Node> nodes;
    private List<Edge> edges;
    private FlowStatus status;
    private Date createTime;
    private Date updateTime;
    
    public void validate() {
        // 验证DAG的合法性
        // 检查是否存在环
        // 检查节点连接的合法性
    }
    
    public ExecutionPlan createExecutionPlan(int concurrency) {
        // 根据DAG生成执行计划
        return new ExecutionPlan(this, concurrency);
    }
    
    public static DataProcessFlow fromJson(String json) {
        // 从JSON创建DataProcessFlow实例
    }
    
    public String toJson() {
        // 将DataProcessFlow实例转换为JSON
    }
} 