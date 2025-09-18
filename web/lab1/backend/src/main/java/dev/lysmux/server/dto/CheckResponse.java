package dev.lysmux.server.dto;

import java.time.LocalTime;

public record CheckResponse(ContainsResponse[] contains, String time, long executionTime) {
    public record ContainsResponse(double x, double y, double r, boolean contains){}
}