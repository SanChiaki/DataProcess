package org.example.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import java.util.Optional;

@Component
class WorkflowPersistenceAdapter {
    private final EntityManager entityManager;
    private final WorkflowMapper mapper;
    
    public Workflow save(Workflow workflow) {
        WorkflowEntity entity = mapper.toEntity(workflow);
        entityManager.persist(entity);
        return mapper.toDomain(entity);
    }
    
    public Optional<Workflow> findById(WorkflowId id) {
        WorkflowEntity entity = entityManager.find(WorkflowEntity.class, id.value());
        return Optional.ofNullable(entity).map(mapper::toDomain);
    }
    
    // ... 其他实现
} 