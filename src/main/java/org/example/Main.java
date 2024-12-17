package org.example;

import com.grapecity.documents.excel.IWorksheet;
import com.grapecity.documents.excel.Workbook;
import com.grapecity.documents.excel.drawing.ChartType;
import com.grapecity.documents.excel.drawing.IShape;
import com.grapecity.documents.excel.drawing.RowCol;

public class Main {
    public static void main(String[] args) {
        testlambda();
    }
    static void test(){
        Workbook workbook = new Workbook();
        IWorksheet worksheet = workbook.getWorksheets().get(0);
        worksheet.getRange("B2").setValue("欢迎体验葡萄城 GcExcel 服务端表格组件");

        worksheet.getRange("B4").getHyperlinks().add(worksheet.getRange("B4"),
                "https://www.grapecity.com.cn/developer/grapecitydocuments/excel-java/docs/getting-started",
                null, "打开产品文档-快速开始页面", "产品文档");

        worksheet.getRange("D4").getHyperlinks().add(worksheet.getRange("D4"),
                "https://demo.grapecity.com.cn/documents-api-excel-java/demos/tutorial",
                null, "打开Demo网站-入门教程", "Demo网站");

        worksheet.getRange("F4").getHyperlinks().add(worksheet.getRange("F4"),
                "https://gcdn.grapecity.com.cn/showforum-230-1.html",
                null, "打开技术社区-SpreadJS & GcExcel专区", "技术社区");
        workbook.save("MyGcExcel.xlsx");
    }

    static void test1(){
            // 创建 workbook
            Workbook workbook = new Workbook();
            IWorksheet worksheet = workbook.getWorksheets().get(0);
            // 准备图表数据
            worksheet.getRange("A1:D4")
                    .setValue(new Object[][]{
                            {null, "Q1", "Q2", "Q3"},
                            {"手机", 1330, 2345, 3493},
                            {"电脑", 2032, 3632, 2197},
                            {"平板", 6233, 3270, 2030}});
            worksheet.getRange("A:D").getColumns().autoFit();
            // 添加饼图
            IShape areaChartShape = worksheet.getShapes().
                    addChart(ChartType.Pie3D, 250, 20, 360, 230);

            // 添加系列
            areaChartShape.getChart().getSeriesCollection().
                    add(worksheet.getRange("A1:D4"), RowCol.Columns, true, true);

            // 修改图表标题
            areaChartShape.getChart().getChartTitle().
                    getTextFrame().getTextRange().getParagraphs()
                    .add("年销售记录");

            // Saving workbook to Xlsx
            workbook.save("output/PieChart.xlsx");

    }

    static void testlambda(){
        Workbook workbook = new Workbook();
        workbook.open("D:\\Workspace\\gcexcel\\GcExcel-Java-7.2.0\\Demo\\GcExcel_Maven\\files\\lambdatest.ssjson");
        IWorksheet worksheet = workbook.getWorksheets().get("Sheet8");
        Object A8 = worksheet.getRange("A8").getValue();
        System.out.println(A8);
    }
}