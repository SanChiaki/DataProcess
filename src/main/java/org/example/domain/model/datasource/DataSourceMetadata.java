package org.example.domain.model.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface DataSource {
    DataSet getData();
    DataSourceMetadata getMetadata();
}

public class DataSourceMetadata {
    private final String name;
    private final String type;
    private final List<String> availableColumns;

    public DataSourceMetadata(String name, String type, List<String> availableColumns) {
        this.name = name;
        this.type = type;
        this.availableColumns = Collections.unmodifiableList(new ArrayList<>(availableColumns));
    }

    // getters
    public String getName() { return name; }
    public String getType() { return type; }
    public List<String> getAvailableColumns() { return availableColumns; }
}

public class DataSet {
    private final List<String> columns;
    private final List<List<Object>> rows;

    public DataSet(List<String> columns, List<List<Object>> rows) {
        this.columns = Collections.unmodifiableList(new ArrayList<>(columns));
        this.rows = rows.stream()
                .map(ArrayList::new)
                .map(Collections::unmodifiableList)
                .collect(Collectors.toList());
    }

    // getters
    public List<String> getColumns() { return columns; }
    public List<List<Object>> getRows() { return rows; }
} 