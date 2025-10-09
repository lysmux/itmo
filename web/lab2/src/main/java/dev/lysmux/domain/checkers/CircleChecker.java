package dev.lysmux.domain.checkers;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CircleChecker implements ContainsChecker{
    @Override
    public boolean contains(double x, double y, double r) {
        return x >= 0 && y >= 0 && (x * x) + (y * y) <= (r * r) / 4;
    }
}
