package dev.lysmux.lab4.domain.checker;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SquareChecker implements HitChecker {
    @Override
    public boolean contains(double x, double y, double r) {
        return x <= 0 && x >= -r  && y <= 0 && y >= -r / 2;
    }
}
