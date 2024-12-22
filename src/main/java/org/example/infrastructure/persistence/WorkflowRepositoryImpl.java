package org.example.infrastructure.persistence;

import org.example.domain.model.workflow.Workflow;
import org.example.domain.model.workflow.WorkflowId;
import org.example.domain.repository.WorkflowRepository;
import org.example.infrastructure.persistence.mapper.WorkflowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WorkflowRepositoryImpl implements WorkflowRepository {
    private final WorkflowMapper workflowMapper;
    private final WorkflowPersistenceAdapter persistenceAdapter;
    
    public WorkflowRepositoryImpl(WorkflowMapper workflowMapper, 
                                WorkflowPersistenceAdapter persistenceAdapter) {
        this.workflowMapper = workflowMapper;
        this.persistenceAdapter = persistenceAdapter;
    }
    
    @Override
    public Workflow save(Workflow workflow) {
        WorkflowEntity entity = persistenceAdapter.toEntity(workflow);
        if (workflowMapper.findById(entity.getId()).isPresent()) {
            workflowMapper.update(entity);
        } else {
            workflowMapper.insert(entity);
        }
        return workflow;
    }
    
    @Override
    public Optional<Workflow> findById(WorkflowId id) {
        return workflowMapper.findById(id.value())
            .map(persistenceAdapter::toDomain);
    }
    
    @Override
    public Workflow createFromJson(String jsonDefinition) {
        return persistenceAdapter.createFromJson(jsonDefinition);
    }
} 