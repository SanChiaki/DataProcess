package org.example.workflow;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.concurrent.*;

public class ProcessGraph {
    private static final int MAX_CONCURRENT = 3;
    private final Graph<Integer, DefaultEdge> graph;
    private final Map<Integer, CompletableFuture<Integer>> processResults;
    private final ExecutorService executorService;

    public ProcessGraph() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        this.processResults = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(MAX_CONCURRENT);
    }

    public void addNode(Integer node) {
        graph.addVertex(node);
    }

    public void addDependency(Integer from, Integer to) {
        graph.addEdge(from, to);
    }

    public Map<Integer, Integer> execute() throws ExecutionException, InterruptedException {
        Map<Integer, Integer> results = new HashMap<>();
        Set<Integer> completed = new HashSet<>();

        while (completed.size() < graph.vertexSet().size()) {
            Set<Integer> readyNodes = findReadyNodes(completed);
            
            // 并发处理准备好的节点
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Integer node : readyNodes) {
                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(
                    () -> processNode(node),
                    executorService
                );
                processResults.put(node, future);
                futures.add(future.thenAccept(result -> results.put(node, result)));
            }

            // 等待当前批次完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
            completed.addAll(readyNodes);
        }

        executorService.shutdown();
        return results;
    }

    private Set<Integer> findReadyNodes(Set<Integer> completed) {
        Set<Integer> readyNodes = new HashSet<>();
        for (Integer node : graph.vertexSet()) {
            if (!completed.contains(node) && !processResults.containsKey(node)) {
                boolean isReady = graph.incomingEdgesOf(node).stream()
                    .map(graph::getEdgeSource)
                    .allMatch(completed::contains);
                if (isReady) {
                    readyNodes.add(node);
                }
            }
        }
        return readyNodes;
    }

    private Integer processNode(Integer node) {
        // 示例处理逻辑：将输入值加倍
        return node * 2;
    }
} 