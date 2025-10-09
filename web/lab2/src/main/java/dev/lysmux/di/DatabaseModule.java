package dev.lysmux.di;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
public class DatabaseModule {
    @Produces
    Connection provideConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    @Produces
    @ApplicationScoped
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:dotsdb");

        return new HikariDataSource(config);
    }
}
