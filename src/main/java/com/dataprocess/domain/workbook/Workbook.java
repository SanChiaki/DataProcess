package com.dataprocess.domain.workbook;

import com.dataprocess.domain.sheet.Sheet;
import com.grapecity.documents.excel.IWorkbook;
import com.grapecity.documents.excel.IWorksheet;

import java.util.ArrayList;
import java.util.List;

public class Workbook {
    private final IWorkbook workbook;
    
    public Workbook(IWorkbook workbook) {
        this.workbook = workbook;
    }
    
    // 基础操作的简化方法
    public Sheet getSheet(int index) {
        IWorksheet worksheet = workbook.getWorksheets().get(index);
        return new Sheet(worksheet);
    }
    
    public Sheet getSheet(String name) {
        IWorksheet worksheet = workbook.getWorksheets().get(name);
        return new Sheet(worksheet);
    }
    
    public Sheet addSheet(String name) {
        IWorksheet worksheet = workbook.getWorksheets().add();
        return new Sheet(worksheet);
    }
    
    public void save(String path) {
        workbook.save(path);
    }
    
    // 提供直接访问底层API的方法
    public IWorkbook getWorkbook() {
        return workbook;
    }
    
    // 提供流式API
//    public Workbook withSheet(String name, Sheet sheet) {
//        IWorksheet worksheet = workbook.getWorksheets().add(name);
//        // TODO: 复制sheet的内容到新worksheet
//        return this;
//    }
    
    // 提供业务层面的便捷方法
    public List<Sheet> getAllSheets() {
        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
            sheets.add(getSheet(i));
        }
        return sheets;
    }
    
    public List<Sheet> getSheetsByNames(List<String> names) {
        List<Sheet> sheets = new ArrayList<>();
        for (String name : names) {
            sheets.add(getSheet(name));
        }
        return sheets;
    }
    
    public void deleteSheet(String name) {
        workbook.getWorksheets().get(name).delete();
    }
    
    public void deleteSheet(int index) {
        workbook.getWorksheets().get(index).delete();
    }
}