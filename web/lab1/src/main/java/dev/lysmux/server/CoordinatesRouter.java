package dev.lysmux.server;

import dev.lysmux.fcgi.Router;
import dev.lysmux.fcgi.annotations.Param;
import dev.lysmux.fcgi.annotations.RouteMapping;
import dev.lysmux.fcgi.enums.HTTPMethod;
import dev.lysmux.fcgi.enums.HTTPParamType;
import dev.lysmux.server.dto.CheckResponse;
import dev.lysmux.server.dto.Coordinates;

import java.util.List;

public class CoordinatesRouter extends Router {
    private final List<ContainsChecker> checkers = List.of(
            CoordinatesRouter::checkInSquare,
            CoordinatesRouter::checkInCircle,
            CoordinatesRouter::checkInTriangle
    );

    @RouteMapping(methods = HTTPMethod.POST, path = "/check")
    public CheckResponse check(@Param(type = HTTPParamType.JSON) Coordinates coordinates) {
        boolean contains = checkers.stream()
                .anyMatch(checker -> checker.contains(
                        coordinates.x(),
                        coordinates.y(),
                        coordinates.r()
                ));

        return new CheckResponse(contains);
    }

    private static boolean checkInSquare(double x, double y, double r) {
        return x <= 0 && x >= -r && y >= 0 && y <= r;
    }

    private static boolean checkInCircle(double x, double y, double r) {
        return x >= 0 && y >= 0 && (x * x) + (y * y) <= r * r;
    }

    private static boolean checkInTriangle(double x, double y, double r) {
        return x >= 0 && x <= r / 2 && y >= 0 && y <= r / 2;
    }
}
