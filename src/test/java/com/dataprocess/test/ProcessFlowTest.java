package com.dataprocess.test;

import com.dataprocess.application.ProcessService;
import com.dataprocess.domain.common.Version;
import com.dataprocess.domain.graph.Port;
import com.dataprocess.domain.graph.ProcessGraph;
import com.dataprocess.domain.operation.Operation;
import com.dataprocess.domain.operation.OperationFactory;
import com.dataprocess.domain.sheet.Sheet;
import com.dataprocess.domain.workbook.Workbook;
import com.dataprocess.domain.workbook.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessFlowTest {
    
    @Test
    public void testMergeThreeSheets() throws Exception {
        // 1. 准备测试数据 - 创建一个包含3个sheet的源文件
        WorkbookFactory workbookFactory = new WorkbookFactory();
        Workbook sourceWorkbook = workbookFactory.createFromFile("src/test/resources/input.xlsx");
        
        // 2. 创建合并操作
        OperationFactory operationFactory = new OperationFactory();
        Operation mergeOperation = operationFactory.createMergeOperation(
            "merge-1", 
            new Version("1.0")
        );
        
        // 3. 获取需要合并的Sheet页
        List<Sheet> sheetsToMerge = Arrays.asList(
            sourceWorkbook.getSheet(0),
            sourceWorkbook.getSheet(1),
            sourceWorkbook.getSheet(2)
        );
        
        // 4. 准备操作的输入
        Map<Port, Sheet> operationInputs = new HashMap<>();
        operationInputs.put(
            mergeOperation.getPortByName("sheets"), 
            sheetsToMerge
        );
        
        // 5. 执行合并操作
        Sheet mergedSheet = mergeOperation.execute(operationInputs);
        assertNotNull(mergedSheet, "合并后的Sheet不应为空");
        
        // 6. 创建输出工作簿并保存结果
        Workbook outputWorkbook = workbookFactory.createEmpty();
        outputWorkbook.addSheet("sheetmerge", mergedSheet);
        outputWorkbook.save("src/test/resources/output.xlsx");
        
        // 7. 验证结果
        Workbook resultWorkbook = workbookFactory.createFromFile(
            "src/test/resources/output.xlsx"
        );
        Sheet resultSheet = resultWorkbook.getSheet(0);
        
        // 验证合并后的数据是否正确
        // TODO: 根据实际业务需求添加更多断言
        assertNotNull(resultSheet.getWorksheet());
        assertTrue(
            resultSheet.getWorksheet().getUsedRange().getRows().getCount() > 1,
            "合并后的Sheet应该包含数据行"
        );
    }
    
    @Test
    public void testProcessFromJson() throws Exception {
        // 1. 准备JSON定义
        String jsonDefinition = "{\n" +
            "  \"nodes\": [\n" +
            "    {\n" +
            "      \"id\": \"input-1\",\n" +
            "      \"type\": \"input\",\n" +
            "      \"config\": {\n" +
            "        \"path\": \"src/test/resources/input.xlsx\",\n" +
            "        \"sheets\": [\"Sheet1\", \"Sheet2\", \"Sheet3\"]\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"merge-1\",\n" +
            "      \"type\": \"merge\",\n" +
            "      \"version\": \"1.0\",\n" +
            "      \"config\": {\n" +
            "        \"outputSheet\": \"sheetmerge\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"output-1\",\n" +
            "      \"type\": \"output\",\n" +
            "      \"config\": {\n" +
            "        \"path\": \"src/test/resources/output.xlsx\"\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"edges\": [\n" +
            "    {\n" +
            "      \"from\": {\"nodeId\": \"input-1\", \"port\": \"sheets\"},\n" +
            "      \"to\": {\"nodeId\": \"merge-1\", \"port\": \"sheets\"}\n" +
            "    },\n" +
            "    {\n" +
            "      \"from\": {\"nodeId\": \"merge-1\", \"port\": \"merged\"},\n" +
            "      \"to\": {\"nodeId\": \"output-1\", \"port\": \"sheet\"}\n" +
            "    }\n" +
            "  ]\n" +
            "}";
        
        // 2. 使用ProcessService解析JSON并执行
        ProcessService processService = new ProcessService();
        ProcessGraph graph = processService.createFromJson(jsonDefinition);
        assertNotNull(graph, "处理图不应为空");
        
        // 3. 验证图的结构
        assertEquals(3, graph.getNodes().size(), "应该有3个节点");
        assertEquals(2, graph.getEdges().size(), "应该有2条边");
        
        // 4. 执行图并验证结果
        // TODO: 实现图执行逻辑并添加相应的断言
    }
} 