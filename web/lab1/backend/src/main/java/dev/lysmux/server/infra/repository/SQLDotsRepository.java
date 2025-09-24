package dev.lysmux.server.infra.repository;

import com.google.inject.Inject;
import dev.lysmux.server.domain.DotCheck;
import dev.lysmux.server.infra.database.SqlExecutor;
import lombok.extern.java.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Log
public class SQLDotsRepository implements DotsRepository {
    private final SqlExecutor sqlExecutor;

    private static final String SQL_GET = "SELECT * FROM dots WHERE x = ? AND y = ? AND r = ?";
    private static final String SQL_ADD = "INSERT INTO dots (x, y, r, contains) VALUES (?, ?, ?, ?)";
    private static final String SQL_REMOVE = "DELETE FROM dots WHERE x = ? AND y = ? AND r = ?";

    @Inject
    public SQLDotsRepository(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public Optional<DotCheck> get(double x, double y, double r) {
        return sqlExecutor.executeQuery(SQL_GET, stmt -> {
            stmt.setDouble(1, x);
            stmt.setDouble(2, y);
            stmt.setDouble(3, r);
        }, rs -> {
            if (!rs.next()) return Optional.empty();
            log.info("Got dot check for (%s, %s, %s)".formatted(x, y, r));

            return Optional.of(mapResultSetToDotCheck(rs));
        });
    }

    @Override
    public void add(DotCheck dotCheck) {
        sqlExecutor.executeUpdate(SQL_ADD, stmt -> {
            stmt.setDouble(1, dotCheck.x());
            stmt.setDouble(2, dotCheck.y());
            stmt.setDouble(3, dotCheck.r());
            stmt.setBoolean(4, dotCheck.contains());
        });

        log.info("Added dot check for (%s, %s, %s)".formatted(dotCheck.x(), dotCheck.y(), dotCheck.r()));
    }

    @Override
    public void remove(double x, double y, double r) {
        sqlExecutor.executeUpdate(SQL_REMOVE, stmt -> {
            stmt.setDouble(1, x);
            stmt.setDouble(2, y);
            stmt.setDouble(3, r);
        });

        log.info("Removed dot check for (%s, %s, %s)".formatted(x, y, r));
    }

    private DotCheck mapResultSetToDotCheck(ResultSet rs) throws SQLException {
        return DotCheck.builder()
                .x(rs.getDouble("x"))
                .y(rs.getDouble("y"))
                .r(rs.getDouble("r"))
                .contains(rs.getBoolean("contains"))
                .build();
    }
}
