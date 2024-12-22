package org.example.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistence.entity.WorkflowEntity;
import java.util.Optional;

@Mapper
public interface WorkflowMapper {
    void insert(WorkflowEntity workflow);
    Optional<WorkflowEntity> findById(String id);
    void update(WorkflowEntity workflow);
    void delete(String id);
} 