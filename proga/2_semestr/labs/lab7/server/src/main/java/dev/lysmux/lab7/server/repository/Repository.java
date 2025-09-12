package dev.lysmux.lab7.server.repository;

import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public abstract class Repository {
    protected final Connection connection;

    protected List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
            }

        return resultList;
    }
}
