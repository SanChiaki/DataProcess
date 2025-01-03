package com.dataprocess.domain.operation;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OperationFactory {
    // 操作类型和版本到实现类的映射
    private final Map<OperationKey, OperationCreator> operationCreators = new ConcurrentHashMap<>();
    
    public Operation createOperation(String type, String version, String id) {
        OperationKey key = new OperationKey(type, version);
        OperationCreator creator = operationCreators.get(key);
        if (creator == null) {
            throw new OperationException(
                String.format("Operation not found: type=%s, version=%s", type, version)
            );
        }
        return creator.create(id);
    }
    
    public void registerOperation(String type, String version, OperationCreator creator) {
        operationCreators.put(new OperationKey(type, version), creator);
    }
    
    private static class OperationKey {
        private final String type;
        private final String version;
        
        public OperationKey(String type, String version) {
            this.type = type;
            this.version = version;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OperationKey that = (OperationKey) o;
            return type.equals(that.type) && version.equals(that.version);
        }
        
        @Override
        public int hashCode() {
            return 31 * type.hashCode() + version.hashCode();
        }
    }
    
    @FunctionalInterface
    public interface OperationCreator {
        Operation create(String id);
    }
} 