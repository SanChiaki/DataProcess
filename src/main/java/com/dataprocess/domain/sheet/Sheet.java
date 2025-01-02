package com.dataprocess.domain.sheet;

import com.grapecity.documents.excel.IRange;
import com.grapecity.documents.excel.IWorksheet;

public class Sheet {
    private final IWorksheet worksheet;

    public Sheet(IWorksheet worksheet) {
        this.worksheet = worksheet;
    }

    // 基础操作的简化方法
    public void setValue(int row, int col, String value) {
        worksheet.getRange(row, col).setValue(value);
    }
    
    public String getValue(int row, int col) {
        return worksheet.getRange(row, col).getText();
    }
    
    // 提供直接访问底层API的方法
    public IWorksheet getWorksheet() {
        return worksheet;
    }
    
    // 提供流式API
    public Sheet withValue(int row, int col, String value) {
        setValue(row, col, value);
        return this;
    }
    
    // 提供业务层面的便捷方法
    public void copyColumn(int sourceCol, Sheet targetSheet, int targetCol, int startRow) {
        IRange sourceRange = worksheet.getRange(0, sourceCol,
            worksheet.getUsedRange().getRows().getCount(), 1);
        sourceRange.copy(targetSheet.getWorksheet().getRange(startRow, targetCol));
    }
}