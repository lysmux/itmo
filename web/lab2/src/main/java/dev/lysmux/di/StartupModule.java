package dev.lysmux.di;


import dev.lysmux.infra.database.LiquibaseMigration;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class StartupModule implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            CDI<Object> cdi = CDI.current();
            LiquibaseMigration migration = cdi.select(LiquibaseMigration.class).get();
            migration.runMigrations();
            log.info("Database migration completed successfully");
        } catch (Exception e) {
            log.error("Database migration failed", e);
            throw new RuntimeException("Migration failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}