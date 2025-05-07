package dev.lysmux.lab8.server.config;

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
