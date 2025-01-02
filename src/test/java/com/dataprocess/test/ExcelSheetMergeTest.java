package com.dataprocess.test;


import org.junit.Test;
import com.grapecity.documents.excel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelSheetMergeTest {
    private static final Logger log = LoggerFactory.getLogger(ExcelSheetMergeTest.class);

    @Test
    public void testExcelMerge() throws Exception {
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
                System.out.println("列数据打印" + columnName1 + columnName2);
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