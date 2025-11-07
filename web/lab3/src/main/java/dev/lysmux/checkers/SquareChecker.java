package dev.lysmux.checkers;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SquareChecker implements ContainsChecker{
    @Override
    public boolean contains(double x, double y, double r) {
        return x <= 0 && x >= -r / 2 && y <= 0 && y >= -r;
    }
}
