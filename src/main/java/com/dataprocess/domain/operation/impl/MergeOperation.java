package com.dataprocess.domain.operation.impl;

import com.dataprocess.domain.operation.Operation;
import com.dataprocess.domain.operation.OperationException;
import com.dataprocess.domain.operation.Port;
import com.dataprocess.domain.operation.Port.PortType;
import com.dataprocess.domain.sheet.Sheet;
import lombok.Getter;

import java.util.*;

// 添加GCExcel相关的导入
import com.grapecity.documents.excel.*;

public class MergeOperation implements Operation {
    @Getter
    private final String id;
    
    @Getter
    private final String type = "MERGE";
    
    @Getter
    private final String version = "1.0";
    
    // 输入端口：支持多个输入
    private final Port mainInput;
    private final Port secondaryInput;
    
    // 输出端口
    private final Port output;
    
    public MergeOperation(String id) {
        this.id = id;
        this.mainInput = new Port("main", PortType.INPUT);
        this.secondaryInput = new Port("secondary", PortType.INPUT_ARRAY);
        this.output = new Port("output", PortType.OUTPUT);
    }
    
    @Override
    public Set<Port> getInputPorts() {
        return new HashSet<>(Arrays.asList(mainInput, secondaryInput));
    }
    
    @Override
    public Set<Port> getOutputPorts() {
        return Collections.singleton(output);
    }
    
    @Override
    public void execute(Map<Port, List<Sheet>> inputData) {
        // 1. 获取输入数据
        List<Sheet> mainSheets = inputData.get(mainInput);
        List<Sheet> secondarySheets = inputData.get(secondaryInput);
        
        if (mainSheets == null || mainSheets.isEmpty()) {
            throw new OperationException("Main input is required");
        }
        
        // 2. 执行合并
        Sheet mergedSheet = mergeSheets(mainSheets.get(0), secondarySheets);
        
        // 3. 设置输出
        output.setData(Collections.singletonList(mergedSheet));
    }
    
    private Sheet mergeSheets(Sheet mainSheet, List<Sheet> secondarySheets) {
        if (secondarySheets == null || secondarySheets.isEmpty()) {
            return mainSheet;
        }
        
        IWorksheet resultSheet = mainSheet.getWorksheet();
        IRange mainHeaderRange = resultSheet.getRange("1:1").getUsedRange();
        
        // 从第一行开始追加数据
        int currentRow = resultSheet.getUsedRange().getRow() + 
                        resultSheet.getUsedRange().getRows().getCount();
        
        // 合并每个辅助Sheet
        for (Sheet secondarySheet : secondarySheets) {
            IWorksheet worksheet = secondarySheet.getWorksheet();
            IRange headerRange = worksheet.getRange("1:1").getUsedRange();
            
            // 构建列映射关系
            Map<Integer, Integer> columnMapping = buildColumnMapping(
                mainHeaderRange, headerRange);
            
            // 获取数据范围（不包括表头）
            IRange dataRange = worksheet.getUsedRange()
                .offset(1, 0, worksheet.getUsedRange().getRows().getCount() - 1, 
                       headerRange.getColumns().getCount());
            
            // 复制数据
            copyData(dataRange, resultSheet, currentRow, columnMapping);
            
            // 更新下一次追加的起始行
            currentRow += dataRange.getRows().getCount();
        }
        
        return new Sheet(resultSheet);
    }
    
    private Map<Integer, Integer> buildColumnMapping(IRange mainHeader, IRange secondaryHeader) {
        Map<Integer, Integer> mapping = new HashMap<>();
        
        // 遍历次要表头的每一列
        for (int srcCol = 0; srcCol < secondaryHeader.getColumns().getCount(); srcCol++) {
            String columnName = secondaryHeader.get(0, srcCol).getText();
            
            // 在主表头中查找匹配的列
            for (int destCol = 0; destCol < mainHeader.getColumns().getCount(); destCol++) {
                if (mainHeader.get(0, destCol).getText().equals(columnName)) {
                    mapping.put(srcCol, destCol);
                    break;
                }
            }
        }
        
        return mapping;
    }
    
    private void copyData(IRange sourceRange, IWorksheet targetSheet, 
                         int targetStartRow, Map<Integer, Integer> columnMapping) {
        // 遍历源数据的每一行
        for (int row = 0; row < sourceRange.getRows().getCount(); row++) {
            // 遍历列映射
            for (Map.Entry<Integer, Integer> entry : columnMapping.entrySet()) {
                int srcCol = entry.getKey();
                int destCol = entry.getValue();
                
                // 获取源单元格的值和格式
                IRange sourceCell = sourceRange.get(row, srcCol);
                IRange targetCell = targetSheet.getRange(targetStartRow + row, destCol);
                
                // 复制值和格式
                copyCell(sourceCell, targetCell);
            }
        }
    }
    
    private void copyCell(IRange source, IRange target) {
        // 复制值
        if (source.getValue() != null) {
            target.setValue(source.getValue());
        }
        
        // 复制格式
        target.setNumberFormat(source.getNumberFormat());
        target.setHorizontalAlignment(source.getHorizontalAlignment());
        target.setVerticalAlignment(source.getVerticalAlignment());
        
        // 复制字体设置
        IFont sourceFont = source.getFont();
        IFont targetFont = target.getFont();
        targetFont.setName(sourceFont.getName());
        targetFont.setSize(sourceFont.getSize());
        targetFont.setBold(sourceFont.getBold());
        targetFont.setItalic(sourceFont.getItalic());
    }
} 