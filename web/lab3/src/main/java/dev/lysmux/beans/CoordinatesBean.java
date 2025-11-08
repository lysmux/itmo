package dev.lysmux.beans;

import dev.lysmux.checkers.ContainsChecker;
import dev.lysmux.db.Result;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Named("coordinates")
@SessionScoped
@Data
@Slf4j
public class CoordinatesBean implements Serializable {
    private Double y = 0d;
    private Double x = 0d;
    private Double r = null;

    @Inject
    private ResultsBean results;

    @Inject
    @Any
    private Instance<ContainsChecker> checkers;

    public void check() {
        log.atInfo()
                .addKeyValue("x", x)
                .addKeyValue("y", y)
                .addKeyValue("r", r)
                .log("check");

        long startTime = System.nanoTime();
        ZonedDateTime time = ZonedDateTime
                .now(ZoneId.of("Europe/Moscow"))
                .truncatedTo(ChronoUnit.SECONDS);

        boolean contains = checkers.stream()
                .anyMatch(checker -> checker.contains(x, y, r));
        Result result = Result.builder()
                .x(x)
                .y(y)
                .r(r)
                .contains(contains)
                .time(time)
                .executionTime(System.nanoTime() - startTime)
                .build();
        results.addResult(result);
    }

    public boolean isValid() {
        return (r != null && r >= 1 && r <= 5)
                && (y != null && y >= -3 && y <= 3)
                && (x != null && x >= -5 && x <= 5);
    }

    public void setX(Double x) {
        this.x = x;
        log.info("setX: {}", x);
    }

    public void setR(Double r) {
        this.r = r;
        log.info("setR: {}", r);
    }

    public void setY(Double y) {
        this.y = y;
        log.info("setY: {}", y);
    }
}
