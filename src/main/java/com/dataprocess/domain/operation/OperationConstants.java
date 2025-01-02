package com.dataprocess.domain.operation;

public final class OperationConstants {
    private OperationConstants() {} // 防止实例化

    public static final class Types {
        public static final String MERGE = "merge";
        public static final String FILTER = "filter";
        public static final String JOIN = "join";
    }

    public static final class Versions {
        public static final class Merge {
            public static final String V1_0 = "1.0";
            public static final String V2_0 = "2.0";
        }
        
        public static final class Filter {
            public static final String V1_0 = "1.0";
        }
        
        public static final class Join {
            public static final String V1_0 = "1.0";
        }
    }
} 