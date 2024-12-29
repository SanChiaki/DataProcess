package com.dataprocess.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;

//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import com.grapecity.documents.excel.*;
import com.grapecity.documents.excel.drawing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.junit.Assert.*;
//
//
//import com.dataprocess.domain.model.DataProcessFlow;
//import com.dataprocess.domain.service.DataProcessService;

public class ExcelSheetMergeTest {
    private static final Logger log = LoggerFactory.getLogger(ExcelSheetMergeTest.class);
//    @Test
//    public void testExcelSheetMerge() throws Exception {
//        // 准备测试数据
//        createTestExcel();
//
//        // 读取流程定义
//        String json = new String(Files.readAllBytes(Paths.get("flow-example.json")));
//        DataProcessFlow flow = DataProcessFlow.fromJson(json);
//
//        // 执行流程
//        DataProcessService service = new DataProcessService();
//        ExecutionResult result = service.executeFlow(flow, 2);
//
//        // 验证结果
//        assertTrue(result.isSuccess());
//        File resultFile = new File("result.xlsx");
//        assertTrue(resultFile.exists());
//
//        // 验证合并结果
//        try (Workbook workbook = WorkbookFactory.create(resultFile)) {
//            Sheet resultSheet = workbook.getSheet("合并结果");
//            assertEquals(201, resultSheet.getLastRowNum()); // 假设每个sheet有100行数据
//        }
//    }
//
//    private static void createTestExcel() throws Exception {
//        try (Workbook workbook = new XSSFWorkbook()) {
//            // 创建Sheet1
//            Sheet sheet1 = workbook.createSheet("Sheet1");
//            createTestData(sheet1, "Sheet1-");
//
//            // 创建Sheet2
//            Sheet sheet2 = workbook.createSheet("Sheet2");
//            createTestData(sheet2, "Sheet2-");
//
//            // 保存测试文件
//            try (FileOutputStream fos = new FileOutputStream("test.xlsx")) {
//                workbook.write(fos);
//            }
//        }
//    }
//
//    private static void createTestData(Sheet sheet, String prefix) {
//        // 创建表头
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"ID", "Name", "Value", "Date"};
//        for (int i = 0; i < headers.length; i++) {
//            headerRow.createCell(i).setCellValue(headers[i]);
//        }
//
//        // 创建数据行
//        for (int i = 1; i <= 100; i++) {
//            Row row = sheet.createRow(i);
//            row.createCell(0).setCellValue(i);
//            row.createCell(1).setCellValue(prefix + "Name" + i);
//            row.createCell(2).setCellValue(new Random().nextDouble() * 100);
//            row.createCell(3).setCellValue(new Date());
//        }
//    }

    @Test
    public  void testExcelMerge() throws Exception {
        // 创建工作簿
        Workbook workbook = new Workbook();

        // 加载 Excel 文件
        workbook.open("input.xlsx");

        // 获取两个 Sheet
        IWorksheet sheet1 = workbook.getWorksheets().get(0); // 第一个 Sheet
        IWorksheet sheet2 = workbook.getWorksheets().get(1); // 第二个 Sheet

        // 获取两个 Sheet 的列名（首行）
        IRange headerRange1 = sheet1.getRange("1:1").getUsedRange(); // 第一个 Sheet 的列名
        IRange headerRange2 = sheet2.getRange("1:1").getUsedRange(); // 第二个 Sheet 的列名

        // 获取第一个 Sheet 的最后一个非空行
        int lastRow = sheet1.getUsedRange().getRow() + sheet1.getUsedRange().getRows().getCount();

        // 遍历第二个 Sheet 的列名，找到与第一个 Sheet 列名匹配的列
        for (int col2 = 0; col2 < headerRange2.getColumns().getCount(); col2++) {
            String columnName2 = headerRange2.get(col2).getText(); // 获取第二个 Sheet 的列名

            // 在第一个 Sheet 中查找匹配的列名
            for (int col1 = 0; col1 < headerRange1.getColumns().getCount(); col1++) {
                String columnName1 = headerRange1.get(col1).getText(); // 获取第一个 Sheet 的列名
                System.out.println("列数据打印"+columnName1+columnName2);
                // 如果列名匹配，则复制数据
                if (columnName1.equals(columnName2)) {
                    // 获取第二个 Sheet 的整列数据（排除首行）
                    IRange sourceColumn = sheet2.getRange(1, col2, sheet2.getUsedRange().getRows().getCount() - 1, 1);

                    // 将数据复制到第一个 Sheet 的对应列
                    sourceColumn.copy(sheet1.getRange(lastRow, col1));
                    break; // 找到匹配列后跳出内层循环
                }
            }
        }

        // 保存合并后的文件
        workbook.save("output.xlsx");
    }
}