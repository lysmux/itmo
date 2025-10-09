package dev.lysmux.infra.repository;


import dev.lysmux.domain.DotCheck;

import java.util.Optional;

public interface DotsRepository {
    Optional<DotCheck> get(double x, double y, double r);

    void add(DotCheck dotCheck);

    void remove(double x, double y, double r);
}
