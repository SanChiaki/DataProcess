public interface DataProcessFlowRepository {
    DataProcessFlow save(DataProcessFlow flow);
    DataProcessFlow findById(FlowId id);
    List<DataProcessFlow> findAll();
} 