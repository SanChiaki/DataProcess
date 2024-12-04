package org.example.workflow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProcessGraphTest {
    
    @Test
    public void testSimpleWorkflow() throws ExecutionException, InterruptedException {
        ProcessGraph graph = new ProcessGraph();
        
        // 构建DAG
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        
        // 添加依赖关系: 1->2->4, 1->3->4
        graph.addDependency(1, 2);
        graph.addDependency(1, 3);
        graph.addDependency(2, 4);
        graph.addDependency(3, 4);
        
        Map<Integer, Integer> results = graph.execute();
        
        // 验证结果
        assertEquals(2, results.get(1));  // 1 * 2 = 2
        assertEquals(4, results.get(2));  // 2 * 2 = 4
        assertEquals(6, results.get(3));  // 3 * 2 = 6
        assertEquals(8, results.get(4));  // 4 * 2 = 8
    }
} 