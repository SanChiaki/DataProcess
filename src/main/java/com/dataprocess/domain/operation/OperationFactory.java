package com.dataprocess.domain.operation;

import com.dataprocess.domain.operation.impl.MergeOperation;
// import com.dataprocess.domain.operation.impl.MergeOperationV2;

public class OperationFactory {
    public Operation createOperation(String type, String version, String id) {
        if (OperationConstants.Types.MERGE.equals(type)) {
            switch (version) {
                case OperationConstants.Versions.Merge.V1_0:
                    return new MergeOperation(id);
                // case OperationConstants.Versions.Merge.V2_0:
                //     return new MergeOperationV2(id);
                default:
                    throw new IllegalArgumentException("Unsupported version: " + version);
            }
        }
        throw new IllegalArgumentException("Unsupported operation type: " + type);
    }
} 