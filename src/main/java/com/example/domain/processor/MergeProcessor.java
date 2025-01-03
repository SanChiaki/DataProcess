package com.example.domain.processor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MergeProcessor implements NodeProcessor {
    @Override
    public WorkbookData process(List<WorkbookData> inputs) {
        // 1. 参数校验
        if (inputs.size() < 2) {
            throw new IllegalArgumentException("Merge operation requires at least 2 inputs");
        }
        
        // 2. 创建新的工作簿
        IWorkbook mergedWorkbook = new GcExcelWorkbook();
        
        // 3. 根据配置进行合并
        for (WorkbookData input : inputs) {
            // 执行具体的合并逻辑
            mergeWorkbooks(mergedWorkbook, input.getWorkbook());
        }
        
        // 4. 返回合并结果
        return new WorkbookData(mergedWorkbook);
    }
} 