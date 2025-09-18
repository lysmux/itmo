package dev.lysmux.server.dto;

public record CheckResponse(ContainsResponse[] contains, long executionTime) {
    public record ContainsResponse(double x, double y, double r, boolean contains){}
}