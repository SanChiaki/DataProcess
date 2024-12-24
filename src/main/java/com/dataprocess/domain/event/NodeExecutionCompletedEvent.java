public class NodeExecutionCompletedEvent extends DomainEvent {
    private final NodeId nodeId;
    private final FlowId flowId;
    private final ExecutionStatus status;
} 