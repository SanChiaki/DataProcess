package com.dataprocess.domain.operation.impl;

import com.dataprocess.domain.common.Version;
import com.dataprocess.domain.graph.Port;
import com.dataprocess.domain.graph.Port.PortType;
import com.dataprocess.domain.operation.Operation;
import com.dataprocess.domain.operation.OperationException;
import com.dataprocess.domain.sheet.Sheet;
import com.grapecity.documents.excel.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MergeOperation implements Operation {
    private final String id;
    private final Set<Port> inputPorts;
    private final Set<Port> outputPorts;

    public MergeOperation(String id) {
        this.id = id;
        
        // 定义一个数组输入端口，用于接收多个Sheet
        this.inputPorts = new HashSet<>();
        this.inputPorts.add(new Port("sheets", PortType.INPUT_ARRAY));
        
        // 定义输出端口
        this.outputPorts = new HashSet<>();
        this.outputPorts.add(new Port("merged", PortType.OUTPUT));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "merge";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public Set<Port> getInputPorts() {
        return inputPorts;
    }

    @Override
    public Set<Port> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public Sheet execute(Map<Port, Sheet> inputs) throws OperationException {
        try {
            // 获取所有输入Sheet
            List<Sheet> sheets = (List<Sheet>) inputs.get(getPortByName("sheets"));
            if (sheets == null || sheets.isEmpty()) {
                throw new OperationException(id, OperationException.FailureType.INVALID_INPUT, "No input sheets provided");
            }

            // 使用第一个Sheet作为基础
            Sheet baseSheet = sheets.get(0);
            IWorksheet resultSheet = baseSheet.getWorksheet();
            IRange baseHeaderRange = resultSheet.getRange("1:1").getUsedRange();
            
            // 从第二个Sheet开始合并
            int lastRow = resultSheet.getUsedRange().getRow() + 
                         resultSheet.getUsedRange().getRows().getCount();

            for (int i = 1; i < sheets.size(); i++) {
                IWorksheet currentSheet = sheets.get(i).getWorksheet();
                IRange currentHeaderRange = currentSheet.getRange("1:1").getUsedRange();

                // 遍历当前Sheet的列名
                for (int col2 = 0; col2 < currentHeaderRange.getColumns().getCount(); col2++) {
                    String columnName2 = currentHeaderRange.get(col2).getText();

                    // 在基础Sheet中查找匹配的列名
                    for (int col1 = 0; col1 < baseHeaderRange.getColumns().getCount(); col1++) {
                        String columnName1 = baseHeaderRange.get(col1).getText();

                        if (columnName1.equals(columnName2)) {
                            // 获取当前Sheet的整列数据（排除首行）
                            IRange sourceColumn = currentSheet.getRange(1, col2, 
                                currentSheet.getUsedRange().getRows().getCount() - 1, 1);

                            // 将数据复制到结果Sheet的对应列
                            sourceColumn.copy(resultSheet.getRange(lastRow, col1));
                            break;
                        }
                    }
                }

                // 更新下一次追加的起始行
                lastRow += currentSheet.getUsedRange().getRows().getCount() - 1;
            }

            return new Sheet(resultSheet);
            
        } catch (Exception e) {
            throw new OperationException(id, OperationException.FailureType.EXECUTION_ERROR, "Failed to merge sheets");
        }
    }

    @Override
    public Port getPortByName(String name) {
        return inputPorts.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Port not found: " + name));
    }
} 