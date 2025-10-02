package dev.lysmux.server.infra.database;

import com.google.inject.Inject;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.java.Log;

import java.sql.Connection;

@Log
public class LiquibaseMigration {
    private final Connection connection;

    @Inject
    public LiquibaseMigration(Connection connection) {
        this.connection = connection;
    }

    public void runMigrations() throws Exception {
        JdbcConnection jdbcConnection = new JdbcConnection(connection);
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(jdbcConnection);
        Liquibase liquibase = new Liquibase(
                "migrations/master.xml",
                new ClassLoaderResourceAccessor(),
                database
        );

        log.info("Running database migrations");
        liquibase.update();
    }
}
