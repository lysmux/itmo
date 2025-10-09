package dev.lysmux.server.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Builder
@Data
public class CheckResult {
    private double x;
    private double y;
    private double r;
    private boolean contains;
    private ZonedDateTime time;
    private long executionTime;
}
