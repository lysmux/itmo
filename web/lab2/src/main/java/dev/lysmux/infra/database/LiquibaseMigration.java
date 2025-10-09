package dev.lysmux.infra.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

@Slf4j
@ApplicationScoped
public class LiquibaseMigration {
    @Inject
    private Connection connection;

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
