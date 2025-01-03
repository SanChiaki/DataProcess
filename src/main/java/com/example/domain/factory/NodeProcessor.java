public interface NodeProcessor {
    WorkbookData process(List<WorkbookData> inputs);
}

@Component
public class NodeProcessorFactory {
    private final Map<String, Map<String, NodeProcessor>> processorVersionMap = new HashMap<>();
    
    public NodeProcessor createProcessor(String type, String version) {
        return Optional.ofNullable(processorVersionMap.get(type))
            .map(versionMap -> versionMap.get(version))
            .orElseThrow(() -> new ProcessorNotFoundException(type, version));
    }
} 