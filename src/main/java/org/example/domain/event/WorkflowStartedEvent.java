package org.example.domain.event;

import org.example.domain.model.workflow.WorkflowId;
import java.time.Instant;
import java.util.Map;

public abstract class WorkflowEvent {
    private final WorkflowId workflowId;
    private final Instant occurredAt;
    
    protected WorkflowEvent(WorkflowId workflowId) {
        this.workflowId = workflowId;
        this.occurredAt = Instant.now();
    }
}

public class WorkflowStartedEvent extends WorkflowEvent {
    public WorkflowStartedEvent(WorkflowId workflowId) {
        super(workflowId);
    }
}

public class WorkflowCompletedEvent extends WorkflowEvent {
    private final Map<String, DataSet> results;
    
    public WorkflowCompletedEvent(WorkflowId workflowId, Map<String, DataSet> results) {
        super(workflowId);
        this.results = results;
    }
} 