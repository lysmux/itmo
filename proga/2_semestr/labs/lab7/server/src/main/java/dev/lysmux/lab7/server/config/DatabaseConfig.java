package dev.lysmux.lab7.server.config;

public record DatabaseConfig(
        String host,
        int port,
        String user,
        String password,
        String dbName
) {
    public String jdbc() {

        return "jdbc:postgresql://" + host + ":" +
                port + "/" +
                dbName;
    }
}
