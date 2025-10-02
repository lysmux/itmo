package dev.lysmux.server.domain.checkers;

public class SquareChecker implements ContainsChecker{
    @Override
    public boolean contains(double x, double y, double r) {
        return x <= 0 && x >= -r && y >= 0 && y <= r;
    }
}
