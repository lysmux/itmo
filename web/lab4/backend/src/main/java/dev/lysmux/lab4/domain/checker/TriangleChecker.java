package dev.lysmux.lab4.domain.checker;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TriangleChecker implements HitChecker {
    @Override
    public boolean contains(double x, double y, double r) {
        return x >= 0 && y >= 0 &&  y <= -2 * x + r;
    }
}
