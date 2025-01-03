package com.dataprocess.domain.flow.node;

import com.dataprocess.domain.flow.Node;
import com.dataprocess.domain.flow.ExecutionContext;
import com.dataprocess.domain.operation.Port;
import com.dataprocess.domain.operation.Port.PortType;
import com.dataprocess.domain.sheet.Sheet;
import com.dataprocess.domain.workbook.Workbook;
import com.dataprocess.domain.workbook.WorkbookFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
public class SheetNode implements Node {
    private String id;
    private String name;
    private SheetConfig config;
    
    // Sheet节点固定有一个输入端口和一个输出端口
    private final Port inputPort = new Port("input", PortType.INPUT_ARRAY);
    private final Port outputPort = new Port("output", PortType.OUTPUT);
    
    private final WorkbookFactory workbookFactory;
    
    public SheetNode(WorkbookFactory workbookFactory) {
        this.workbookFactory = workbookFactory;
    }
    
    @Data
    public static class SheetConfig {
        private String workbookPath;
        private String sheetName;
    }
    
    @Override
    public Set<Port> getInputPorts() {
        return Collections.singleton(inputPort);
    }
    
    @Override
    public Set<Port> getOutputPorts() {
        return Collections.singleton(outputPort);
    }
    
    @Override
    public void execute(ExecutionContext context) {
        // 1. 加载workbook
        Workbook workbook = workbookFactory.createFromFile(config.getWorkbookPath());
        // 注册到上下文以便资源清理
        context.addResource(workbook);
        
        // 2. 获取sheet
        Sheet sheet = workbook.getSheet(config.getSheetName());
        if (sheet == null) {
            throw new IllegalStateException(
                String.format("Sheet %s not found in workbook %s", 
                    config.getSheetName(), config.getWorkbookPath())
            );
        }
        
        // 3. 设置输出端口的数据
        outputPort.setData(Collections.singletonList(sheet));
    }
} 