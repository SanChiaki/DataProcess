public class ProcessExecutor {
    private CompletableFuture<Void> executeNode(Node node, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 1. 获取输入数据
                List<WorkbookData> inputs = getNodeInputs(node);
                
                // 2. 创建对应版本的处理器
                NodeProcessor processor = processorFactory.createProcessor(
                    node.getType(), 
                    node.getVersion()
                );
                
                // 3. 处理数据
                WorkbookData output = processor.process(inputs);
                
                // 4. 存储结果
                saveNodeOutput(node, output);
                
                return null;
            } catch (Exception e) {
                throw new NodeExecutionException(node.getId(), e);
            }
        }, executorService);
    }
    
    private List<WorkbookData> getNodeInputs(Node node) {
        return dag.getIncomingEdges(node).stream()
            .sorted(Comparator.comparing(edge -> ((Edge)edge).getOrder()))
            .map(edge -> getNodeOutput(dag.getEdgeSource(edge)))
            .collect(Collectors.toList());
    }
} 