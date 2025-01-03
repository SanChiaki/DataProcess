public class ProcessEngine {
    private final NodeProcessorFactory nodeProcessorFactory;
    
    public ProcessExecutor buildExecutor(ProcessDefinition definition) {
        // 1. 构建DAG
        DirectedAcyclicGraph<Node, Edge> dag = buildDag(definition);
        
        // 2. 验证图的合法性
        validateDag(dag);
        
        // 3. 创建执行器
        return new ProcessExecutor(dag, definition.getExecutionConfig(), nodeProcessorFactory);
    }
    
    private DirectedAcyclicGraph<Node, Edge> buildDag(ProcessDefinition definition) {
        DirectedAcyclicGraph<Node, Edge> dag = new DirectedAcyclicGraph<>();
        
        // 1. 添加节点
        for (Node node : definition.getNodes()) {
            dag.addVertex(node);
        }
        
        // 2. 添加边（按order排序）
        definition.getEdges().stream()
            .sorted(Comparator.comparing(Edge::getOrder, Comparator.nullsLast(Comparator.naturalOrder())))
            .forEach(edge -> {
                Node from = findNode(definition.getNodes(), edge.getFrom());
                Node to = findNode(definition.getNodes(), edge.getTo());
                dag.addEdge(from, to, edge);
            });
            
        return dag;
    }
    
    private void validateDag(DirectedAcyclicGraph<Node, Edge> dag) {
        // 1. 检查是否有环
        if (dag.hasCycle()) {
            throw new InvalidProcessDefinitionException("Process definition contains cycle");
        }
        
        // 2. 检查输入输出节点
        validateIONodes(dag);
    }
}

public class ProcessExecutor {
    private final DirectedAcyclicGraph<Node, Edge> dag;
    private final ExecutionConfig config;
    private final NodeProcessorFactory processorFactory;
    
    public CompletableFuture<Void> execute() {
        // 1. 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(config.getMaxConcurrency());
        
        // 2. 获取拓扑排序
        List<List<Node>> levels = dag.getTopologicalLevels();
        
        // 3. 按层级并发执行
        return CompletableFuture.runAsync(() -> {
            try {
                for (List<Node> level : levels) {
                    List<CompletableFuture<Void>> futures = level.stream()
                        .map(node -> executeNode(node, executorService))
                        .collect(Collectors.toList());
                    
                    // 等待当前层级所有节点执行完成
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                }
            } finally {
                executorService.shutdown();
            }
        });
    }
} 