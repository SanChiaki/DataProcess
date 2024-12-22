package org.example.application.service;

import org.example.domain.model.workflow.Workflow;
import org.example.domain.service.WorkflowExecutionService;
import org.example.application.dto.WorkflowDTO;
import org.example.domain.repository.WorkflowRepository;

@Service
public class WorkflowApplicationService {
    private final WorkflowRepository workflowRepository;
    private final WorkflowExecutionService executionService;
    
    public WorkflowDTO createWorkflow(String name, String jsonDefinition) {
        Workflow workflow = workflowRepository.createFromJson(jsonDefinition);
        workflow = workflowRepository.save(workflow);
        return WorkflowDTO.fromDomain(workflow);
    }
    
    public WorkflowExecutionDTO executeWorkflow(String workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new WorkflowNotFoundException(workflowId));
            
        WorkflowExecution execution = executionService.executeWorkflow(workflow);
        return WorkflowExecutionDTO.fromDomain(execution);
    }
} 