package org.example.domain.service;

import org.example.domain.model.workflow.Workflow;
import org.example.domain.model.workflow.WorkflowExecution;
import java.util.concurrent.ExecutorService;

public class WorkflowExecutionService {
    private final ExecutorService executorService;
    
    public WorkflowExecution executeWorkflow(Workflow workflow) {
        WorkflowExecution execution = new WorkflowExecution(workflow);
        execution.start(executorService);
        return execution;
    }
} 