package org.example.interfaces.web;

import org.example.application.dto.WorkflowDTO;
import org.example.application.service.WorkflowApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {
    private final WorkflowApplicationService workflowService;

    public WorkflowController(WorkflowApplicationService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping
    public ResponseEntity<WorkflowDTO> createWorkflow(
            @RequestParam String name,
            @RequestBody String jsonDefinition) {
        WorkflowDTO workflow = workflowService.createWorkflow(name, jsonDefinition);
        return ResponseEntity.ok(workflow);
    }

    @PostMapping("/{workflowId}/execute")
    public ResponseEntity<WorkflowExecutionDTO> executeWorkflow(
            @PathVariable String workflowId) {
        WorkflowExecutionDTO execution = workflowService.executeWorkflow(workflowId);
        return ResponseEntity.ok(execution);
    }
} 