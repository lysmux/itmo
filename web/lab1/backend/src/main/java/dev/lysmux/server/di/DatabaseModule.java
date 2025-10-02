package dev.lysmux.server.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.lysmux.server.infra.database.LiquibaseMigration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseModule extends AbstractModule {
    @Provides
    Connection provideConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    @Provides
    @Singleton
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:dotsdb");

        return new HikariDataSource(config);
    }

    @Provides
    @Singleton
    LiquibaseMigration provideLiquibaseMigration(Connection connection) {
        return new LiquibaseMigration(connection);
    }
}
