package dev.lysmux.lab4.domain.repository;

import dev.lysmux.lab4.domain.model.HitResult;
import jakarta.validation.Valid;

import java.util.List;

public interface HitRepository {
    void addHit(HitResult hitResult);

    List<HitResult> getHits();

    HitResult getHit(String id);

    List<HitResult> getUserHits(String userId);

    void clearUserHits(String userId);
}
