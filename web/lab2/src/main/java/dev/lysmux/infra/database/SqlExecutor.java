package dev.lysmux.infra.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@ApplicationScoped
public class SqlExecutor {
    @Inject
    private Provider<Connection> connectionProvider;

    public <T> T executeQuery(final String sql, SqlConsumer paramSetter, SqlFunction<T> handler) {
        try (
                Connection connection = connectionProvider.get();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            paramSetter.accept(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                return handler.apply(rs);
            }
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Error executing SQL")
                    .setCause(e)
                    .addKeyValue("sql", sql)
                    .log();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    public int executeUpdate(String sql, SqlConsumer paramSetter) {
        try (
                Connection connection = connectionProvider.get();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            paramSetter.accept(stmt);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Error executing SQL")
                    .setCause(e)
                    .addKeyValue("sql", sql)
                    .log();
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
