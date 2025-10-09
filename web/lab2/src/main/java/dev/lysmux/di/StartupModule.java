package dev.lysmux.di;


import dev.lysmux.infra.database.LiquibaseMigration;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class StartupModule implements ServletContextListener {

    @Inject
    private LiquibaseMigration migration;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            migration.runMigrations();
            log.info("Database migration completed successfully");
        } catch (Exception e) {
            log.error("Database migration failed: " + e.getMessage());
            throw new RuntimeException("Migration failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup if needed
    }
}