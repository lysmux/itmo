package dev.lysmux.fcgi.exception;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(String path) {
        super("Route not found for path: %s".formatted(path));
    }
}
