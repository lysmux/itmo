package dev.lysmux.infra.database;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.Map;

@Slf4j
@Dependent
public class LiquibaseMigration {
    @Inject
    private Connection connection;

    public void runMigrations() throws Exception {
        try (JdbcConnection jdbcConnection = new JdbcConnection(connection);){
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(jdbcConnection);
            Scope.child(Map.of(), () -> {
                Liquibase liquibase = new Liquibase(
                        "migrations/master.xml",
                        new ClassLoaderResourceAccessor(),
                        database
                );
                log.info("Running database migrations");
                liquibase.update();
            });
        }
    }
}
