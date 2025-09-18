package dev.lysmux.server;

import dev.lysmux.fcgi.Router;
import dev.lysmux.fcgi.annotations.Param;
import dev.lysmux.fcgi.annotations.RouteMapping;
import dev.lysmux.fcgi.dto.Response;
import dev.lysmux.fcgi.enums.HTTPMethod;
import dev.lysmux.fcgi.enums.HTTPParamType;
import dev.lysmux.server.dto.CheckResponse;
import dev.lysmux.server.dto.Coordinates;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CoordinatesRouter extends Router {
    private final List<ContainsChecker> checkers = List.of(
            CoordinatesRouter::checkInSquare,
            CoordinatesRouter::checkInCircle,
            CoordinatesRouter::checkInTriangle
    );

    @RouteMapping(methods = HTTPMethod.POST, path = "/check")
    public CheckResponse check(@Param(type = HTTPParamType.JSON) Coordinates coordinates) {
        long startTime = System.nanoTime();
        LocalTime time = LocalTime.now().withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<CheckResponse.ContainsResponse> responses = new ArrayList<>();

        for (double x : coordinates.x()) {
            for (double r : coordinates.r()) {
                boolean contains = checkers.stream()
                        .anyMatch(checker -> checker.contains(
                                x,
                                coordinates.y(),
                                r
                        ));
                responses.add(new CheckResponse.ContainsResponse(x, coordinates.y(), r, contains));
            }
        }

        return new CheckResponse(
                responses.toArray(CheckResponse.ContainsResponse[]::new),
                time.format(formatter),
                System.nanoTime() - startTime
        );
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
