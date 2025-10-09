package dev.lysmux.infra.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlFunction<R> {
    R apply(ResultSet rs) throws SQLException;
}
