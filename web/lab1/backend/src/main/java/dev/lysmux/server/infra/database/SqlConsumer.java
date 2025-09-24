package dev.lysmux.server.infra.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlConsumer {
    void accept(PreparedStatement stmt) throws SQLException;
}