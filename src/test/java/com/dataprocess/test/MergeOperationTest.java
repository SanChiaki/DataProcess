package com.dataprocess.test;

import com.dataprocess.domain.graph.Port;
import com.dataprocess.domain.operation.Operation;
import com.dataprocess.domain.operation.OperationConstants;
import com.dataprocess.domain.operation.OperationFactory;
import com.dataprocess.domain.sheet.Sheet;
import com.dataprocess.domain.workbook.Workbook;
import com.dataprocess.domain.workbook.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MergeOperationTest {
    private WorkbookFactory workbookFactory;
    private OperationFactory operationFactory;
    
    @BeforeEach
    void setUp() {
        workbookFactory = new WorkbookFactory();
        operationFactory = new OperationFactory();
        
        // 创建测试数据
        createTestWorkbook();
    }
    
    private void createTestWorkbook() {
        Workbook workbook = workbookFactory.createEmpty();
        
        // 创建第一个Sheet
        Sheet sheet1 = workbook.addSheet("Sheet1");
        sheet1.setValue(0, 0, "姓名");
        sheet1.setValue(0, 1, "年龄");
        sheet1.setValue(0, 2, "部门");
        sheet1.setValue(1, 0, "张三");
        sheet1.setValue(1, 1, "25");
        sheet1.setValue(1, 2, "技术部");
        
        // 创建第二个Sheet
        Sheet sheet2 = workbook.addSheet("Sheet2");
        sheet2.setValue(0, 0, "姓名");
        sheet2.setValue(0, 1, "年龄");
        sheet2.setValue(0, 2, "部门");
        sheet2.setValue(1, 0, "李四");
        sheet2.setValue(1, 1, "30");
        sheet2.setValue(1, 2, "市场部");
        
        // 创建第三个Sheet
        Sheet sheet3 = workbook.addSheet("Sheet3");
        sheet3.setValue(0, 0, "姓名");
        sheet3.setValue(0, 1, "年龄");
        sheet3.setValue(0, 2, "部门");
        sheet3.setValue(1, 0, "王五");
        sheet3.setValue(1, 1, "28");
        sheet3.setValue(1, 2, "财务部");
        
        // 保存测试文件
        workbook.save("src/test/resources/input.xlsx");
    }
    
    @Test
    void testMergeOperation() throws Exception {
        // 1. 加载测试数据
        Workbook sourceWorkbook = workbookFactory.createFromFile("src/test/resources/input.xlsx");
        List<Sheet> sheetsToMerge = Arrays.asList(
            sourceWorkbook.getSheet(0),
            sourceWorkbook.getSheet(1),
            sourceWorkbook.getSheet(2)
        );
        
        // 2. 创建合并操作
        Operation mergeOperation = operationFactory.createOperation(
            OperationConstants.Types.MERGE,
            OperationConstants.Versions.Merge.V1_0,
            "test-merge"
        );
        
        // 3. 准备输入数据
        Map<Port, List<Sheet>> inputs = new HashMap<>();
        inputs.put(mergeOperation.getPortByName("sheets"), sheetsToMerge);
        
        // 4. 执行合并
        Sheet mergedSheet = mergeOperation.execute(inputs).get(0);
        
        // 5. 保存结果
        // Workbook resultWorkbook = workbookFactory.createEmpty();
        // resultWorkbook.addSheet("merged", mergedSheet);
        sourceWorkbook.save("src/test/resources/merge-result.xlsx");
        
        // 6. 验证结果
        assertNotNull(mergedSheet);
        assertEquals(4, mergedSheet.getWorksheet().getRowCount(), "应该有4行数据（1个表头+3行数据）");
        assertEquals(3, mergedSheet.getWorksheet().getColumnCount(), "应该有3列数据");
        
        // 验证表头
        assertEquals("姓名", mergedSheet.getValue(0, 0));
        assertEquals("年龄", mergedSheet.getValue(0, 1));
        assertEquals("部门", mergedSheet.getValue(0, 2));
        
        // 验证数据行
        assertEquals("张三", mergedSheet.getValue(1, 0));
        assertEquals("25", mergedSheet.getValue(1, 1));
        assertEquals("技术部", mergedSheet.getValue(1, 2));
        
        assertEquals("李四", mergedSheet.getValue(2, 0));
        assertEquals("30", mergedSheet.getValue(2, 1));
        assertEquals("市场部", mergedSheet.getValue(2, 2));
        
        assertEquals("王五", mergedSheet.getValue(3, 0));
        assertEquals("28", mergedSheet.getValue(3, 1));
        assertEquals("财务部", mergedSheet.getValue(3, 2));
    }
} 