package dev.lysmux.server.api.router;

import com.google.inject.Inject;
import dev.lysmux.fcgi.Router;
import dev.lysmux.fcgi.annotations.Param;
import dev.lysmux.fcgi.annotations.RouteMapping;
import dev.lysmux.fcgi.enums.HTTPMethod;
import dev.lysmux.fcgi.enums.HTTPParamType;
import dev.lysmux.server.api.dto.CheckResponse;
import dev.lysmux.server.api.dto.Coordinates;
import dev.lysmux.server.domain.DotCheck;
import dev.lysmux.server.service.DotsService;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DotsRouter extends Router {
    private final DotsService dotsService;

    @Inject
    private DotsRouter(DotsService dotsService) {
        this.dotsService = dotsService;
    }

    @RouteMapping(methods = HTTPMethod.POST, path = "/check")
    public CheckResponse check(@Param(type = HTTPParamType.JSON) Coordinates coordinates) {
        long startTime = System.nanoTime();
        ZonedDateTime time = ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS);

        DotCheck[] checks = dotsService.check(coordinates.x(), coordinates.y(), coordinates.r());

        return new CheckResponse(
                checks,
                time.format(DateTimeFormatter.ISO_INSTANT),
                System.nanoTime() - startTime
        );
    }
}
