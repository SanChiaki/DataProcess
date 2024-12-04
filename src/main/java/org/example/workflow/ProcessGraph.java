package org.example.workflow;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.*;
import java.util.concurrent.*;

public class ProcessGraph {
    private static final int MAX_CONCURRENT = 3;
    private final Graph<Integer, DefaultEdge> graph;
    private final ExecutorService executorService;

    public ProcessGraph() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        this.executorService = Executors.newFixedThreadPool(MAX_CONCURRENT);
    }

    public void addNode(Integer node) {
        graph.addVertex(node);
    }

    public void addDependency(Integer from, Integer to) {
        graph.addEdge(from, to);
    }

    public Map<Integer, Integer> execute() throws ExecutionException, InterruptedException {
        Map<Integer, Integer> results = new ConcurrentHashMap<>();
        TopologicalOrderIterator<Integer, DefaultEdge> iterator = new TopologicalOrderIterator<>(graph);
        
        // 按层次分组存储节点
        List<Set<Integer>> layers = new ArrayList<>();
        Map<Integer, Integer> nodeToLayer = new HashMap<>();
        
        // 使用拓扑排序确定每个节点的层次
        while (iterator.hasNext()) {
            Integer node = iterator.next();
            int layer = getNodeLayer(node, nodeToLayer);
            
            if (layers.size() <= layer) {
                layers.add(new HashSet<>());
            }
            layers.get(layer).add(node);
        }

        // 按层次顺序执行节点
        for (Set<Integer> layerNodes : layers) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            
            // 同一层的节点可以并发执行
            for (Integer node : layerNodes) {
                CompletableFuture<Void> future = CompletableFuture.supplyAsync(
                        () -> processNode(node),
                        executorService
                    )
                    .thenAccept(result -> results.put(node, result));
                futures.add(future);
            }
            
            // 等待当前层的所有节点执行完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        }

        executorService.shutdown();
        return results;
    }

    private int getNodeLayer(Integer node, Map<Integer, Integer> nodeToLayer) {
        if (nodeToLayer.containsKey(node)) {
            return nodeToLayer.get(node);
        }

        if (graph.inDegreeOf(node) == 0) {
            nodeToLayer.put(node, 0);
            return 0;
        }

        int maxPredecessorLayer = -1;
        for (DefaultEdge edge : graph.incomingEdgesOf(node)) {
            Integer predecessor = graph.getEdgeSource(edge);
            maxPredecessorLayer = Math.max(maxPredecessorLayer, 
                getNodeLayer(predecessor, nodeToLayer));
        }

        int layer = maxPredecessorLayer + 1;
        nodeToLayer.put(node, layer);
        return layer;
    }

    private Integer processNode(Integer node) {
        // 示例处理逻辑：将输入值加倍
        return node * 2;
    }
} 