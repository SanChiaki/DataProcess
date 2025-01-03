package com.dataprocess.domain.operation.impl;

import com.dataprocess.domain.operation.Operation;
import com.dataprocess.domain.operation.Port;
import com.dataprocess.domain.sheet.Sheet;
import com.grapecity.documents.excel.IWorkbook;
import com.grapecity.documents.excel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MergeOperationTest {
    private Operation mergeOperation;
    private Sheet mainSheet;
    private Sheet secondarySheet;
    
    @BeforeEach
    void setUp() {
        // 创建操作实例
        mergeOperation = new MergeOperation("test_merge");
        
        // 准备测试数据
        mainSheet = createTestSheet(new String[][]{
            {"Name", "Age"},
            {"John", "30"},
            {"Alice", "25"}
        });
        
        secondarySheet = createTestSheet(new String[][]{
            {"Name", "Age"},
            {"Bob", "35"},
            {"Carol", "28"}
        });
    }
    
    @Test
    void testMergeOperation() {
        // 准备输入数据
        Map<Port, List<Sheet>> inputData = new HashMap<>();
        Port mainInput = findPort(mergeOperation.getInputPorts(), "main");
        Port secondaryInput = findPort(mergeOperation.getInputPorts(), "secondary");
        
        inputData.put(mainInput, Collections.singletonList(mainSheet));
        inputData.put(secondaryInput, Collections.singletonList(secondarySheet));
        
        // 执行合并
        mergeOperation.execute(inputData);
        
        // 获取输出
        Port output = mergeOperation.getOutputPorts().iterator().next();
        List<Sheet> result = output.getData();
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        
        Sheet mergedSheet = result.get(0);
        assertEquals(4, mergedSheet.getWorksheet().getUsedRange().getRows().getCount());
        assertEquals("Bob", mergedSheet.getValue(2, 0));
        assertEquals("28", mergedSheet.getValue(3, 1));
    }
    
    private Sheet createTestSheet(String[][] data) {
        IWorkbook workbook = new Workbook();
        Sheet sheet = new Sheet(workbook.getWorksheets().get(0));
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                sheet.setValue(i, j, data[i][j]);
            }
        }
        
        return sheet;
    }
    
    private Port findPort(Set<Port> ports, String id) {
        return ports.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Port not found: " + id));
    }
} 