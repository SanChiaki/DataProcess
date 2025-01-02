package com.dataprocess.test;

import com.dataprocess.domain.operation.Port;
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
        Sheet sheet1 = workbook.addSheet("xSheet1");
        sheet1.setValue(0, 0, "姓名");
        sheet1.setValue(0, 1, "年龄");
        sheet1.setValue(0, 2, "部门");
        sheet1.setValue(1, 0, "张三");
        sheet1.setValue(1, 1, "25");
        sheet1.setValue(1, 2, "技术部");
        
        // 创建第二个Sheet
        Sheet sheet2 = workbook.addSheet("xSheet2");
        sheet2.setValue(0, 0, "姓名");
        sheet2.setValue(0, 1, "年龄");
        sheet2.setValue(0, 2, "部门");
        sheet2.setValue(1, 0, "李四");
        sheet2.setValue(1, 1, "30");
        sheet2.setValue(1, 2, "市场部");
        
        // 创建第三个Sheet
        Sheet sheet3 = workbook.addSheet("xSheet3");
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
            sourceWorkbook.getSheet(1),
            sourceWorkbook.getSheet(2),
            sourceWorkbook.getSheet(3)
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

    @Test
    void testMergeOperationDifferentWorkbook() throws Exception {
        // 1. 加载测试数据
        Workbook sourceWorkbook1 = workbookFactory.createFromFile("src/test/resources/input.xlsx");
        Workbook sourceWorkbook2 = workbookFactory.createFromFile("src/test/resources/input0.xlsx");
        List<Sheet> sheetsToMerge = Arrays.asList(
            sourceWorkbook1.getSheet(1),
            sourceWorkbook1.getSheet(2),
            sourceWorkbook2.getSheet(1)
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
        sourceWorkbook1.save("src/test/resources/merge-result-diff.xlsx");
        
        // 6. 验证结果
        assertNotNull(mergedSheet);
        assertEquals(8, mergedSheet.getWorksheet().getRowCount(), "应该有4行数据（1个表头+3行数据）");
        assertEquals(3, mergedSheet.getWorksheet().getColumnCount(), "应该有3列数据");

//        // 验证表头
//        assertEquals("姓名", mergedSheet.getValue(0, 0));
//        assertEquals("年龄", mergedSheet.getValue(0, 1));
//        assertEquals("部门", mergedSheet.getValue(0, 2));
//
//        // 验证数据行
//        assertEquals("张三", mergedSheet.getValue(1, 0));
//        assertEquals("25", mergedSheet.getValue(1, 1));
//        assertEquals("技术部", mergedSheet.getValue(1, 2));
//
//        assertEquals("李四", mergedSheet.getValue(2, 0));
//        assertEquals("30", mergedSheet.getValue(2, 1));
//        assertEquals("市场部", mergedSheet.getValue(2, 2));
//
//        assertEquals("王五", mergedSheet.getValue(3, 0));
//        assertEquals("28", mergedSheet.getValue(3, 1));
//        assertEquals("财务部", mergedSheet.getValue(3, 2));
    }

    @Test
    void 跨工作簿复制() throws Exception {
        Workbook sourceWorkbook1 = workbookFactory.createFromFile("src/test/resources/input.xlsx");
        Workbook sourceWorkbook2 = workbookFactory.createFromFile("src/test/resources/input0.xlsx");
        sourceWorkbook2.getSheet(1).getWorksheet().copy(sourceWorkbook1.getWorkbook());
        sourceWorkbook1.save("src/test/resources/copy-result-1.xlsx");
        sourceWorkbook1.addSheet("sheetxxxx",sourceWorkbook2.getSheet(1));
//        sourceWorkbook2.getSheet(1).getWorksheet().copy(sourceWorkbook1.getWorkbook());
        sourceWorkbook1.save("src/test/resources/copy-result-2.xlsx");
    }

    @Test
    void testMergeToMerge() throws Exception {
        // 1. 加载测试数据
        Workbook sourceWorkbook = workbookFactory.createFromFile("src/test/resources/input.xlsx");

        // 2. 第一次merge：合并1、2表
        Operation mergeOp1 = operationFactory.createOperation(
            OperationConstants.Types.MERGE,
            OperationConstants.Versions.Merge.V1_0,
            "merge-1"
        );

        Map<Port, List<Sheet>> inputs1 = new HashMap<>();
        inputs1.put(mergeOp1.getPortByName("sheets"), Arrays.asList(
            sourceWorkbook.getSheet(1),
            sourceWorkbook.getSheet(2)
        ));

        Sheet mergedSheet1 = mergeOp1.execute(inputs1).get(0);

        // 3. 第二次merge：合并3表
        Operation mergeOp2 = operationFactory.createOperation(
            OperationConstants.Types.MERGE,
            OperationConstants.Versions.Merge.V1_0,
            "merge-2"
        );

        Map<Port, List<Sheet>> inputs2 = new HashMap<>();
        inputs2.put(mergeOp2.getPortByName("sheets"), Arrays.asList(
            sourceWorkbook.getSheet(3)
        ));

        Sheet mergedSheet2 = mergeOp2.execute(inputs2).get(0);

        // 4. 最终merge：合并两个merge结果
        Operation finalMerge = operationFactory.createOperation(
            OperationConstants.Types.MERGE,
            OperationConstants.Versions.Merge.V1_0,
            "merge-final"
        );

        Map<Port, List<Sheet>> inputsFinal = new HashMap<>();
        inputsFinal.put(finalMerge.getPortByName("sheets"), Arrays.asList(
            mergedSheet1,
            mergedSheet2
        ));

        Sheet finalSheet = finalMerge.execute(inputsFinal).get(0);

        // 5. 保存结果
        Workbook resultWorkbook = workbookFactory.createEmpty();
        resultWorkbook.addSheet("merged", finalSheet);
        resultWorkbook.save("src/test/resources/merge-to-merge-result.xlsx");

        // 6. 验证结果
        assertNotNull(finalSheet);
        assertEquals(4, finalSheet.getWorksheet().getRowCount(), "应该有4行数据（1个表头+3行数据）");
        assertEquals(3, finalSheet.getWorksheet().getColumnCount(), "应该有3列数据");

        // 验证表头
        assertEquals("姓名", finalSheet.getValue(0, 0));
        assertEquals("年龄", finalSheet.getValue(0, 1));
        assertEquals("部门", finalSheet.getValue(0, 2));

        // 验证数据行
        assertEquals("张三", finalSheet.getValue(1, 0));
        assertEquals("李四", finalSheet.getValue(2, 0));
        assertEquals("王五", finalSheet.getValue(3, 0));
    }
} 