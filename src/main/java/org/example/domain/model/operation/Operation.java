package org.example.domain.model.operation;

import org.example.domain.model.datasource.DataSet;
import java.util.Map;

public interface Operation {
    OperationResult execute(OperationContext context);
    Version getVersion();
    boolean validate(OperationContext context);
}

public record OperationContext(
    Map<String, DataSet> inputs,
    Map<String, Object> parameters
) {}

public record OperationResult(
    DataSet output,
    OperationStatus status,
    String message
) {}

public enum OperationStatus {
    SUCCESS, FAILED, CANCELLED
} 