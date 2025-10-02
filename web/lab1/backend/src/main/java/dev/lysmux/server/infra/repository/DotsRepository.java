package dev.lysmux.server.infra.repository;

import dev.lysmux.server.domain.DotCheck;

import java.util.Optional;

public interface DotsRepository {
    Optional<DotCheck> get(double x, double y, double r);

    void add(DotCheck dotCheck);

    void remove(double x, double y, double r);
}
