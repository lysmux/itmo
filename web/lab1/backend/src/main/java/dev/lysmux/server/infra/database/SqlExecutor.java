package dev.lysmux.server.infra.database;

import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SqlExecutor {
    private final Provider<Connection> connectionProvider;

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
            throw new DatabaseException();
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
            throw new DatabaseException();
        }
    }
}
