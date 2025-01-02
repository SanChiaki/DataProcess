package com.dataprocess.domain.workbook;

import com.grapecity.documents.excel.Workbook;

public class WorkbookFactory {
    private static final String LICENSE_KEY = "你的注册码";
    private static boolean initialized = false;
    
    private synchronized void initializeIfNeeded() {
        if (!initialized) {
//            有可用Key时启用
//            Workbook.SetLicenseKey(LICENSE_KEY);
            initialized = true;
        }
    }
    
    public com.dataprocess.domain.workbook.Workbook createFromFile(String path) {
        initializeIfNeeded();
        Workbook gcWorkbook = new Workbook();
        gcWorkbook.open(path);
        return new com.dataprocess.domain.workbook.Workbook(gcWorkbook);
    }
    
    public com.dataprocess.domain.workbook.Workbook createEmpty() {
        initializeIfNeeded();
        Workbook gcWorkbook = new Workbook();
        return new com.dataprocess.domain.workbook.Workbook(gcWorkbook);
    }
} 