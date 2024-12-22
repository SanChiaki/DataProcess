package org.example.domain.repository;

import org.example.domain.model.workflow.Workflow;
import org.example.domain.model.workflow.WorkflowId;
import java.util.Optional;

public interface WorkflowRepository {
    Workflow save(Workflow workflow);
    Optional<Workflow> findById(WorkflowId id);
    Workflow createFromJson(String jsonDefinition);
} 