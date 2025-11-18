package dev.lysmux.lab4.repository.memory;

import dev.lysmux.lab4.domain.model.HitResult;
import dev.lysmux.lab4.domain.repository.HitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class MemoryHitRepository implements HitRepository {
    @Getter
    private final List<HitResult> hits = new ArrayList<>();

    public void addHit(HitResult hitResult) {
        hits.add(hitResult);
    }

    public HitResult getHit(String id) {
        return hits.stream()
                .filter(hit -> hit.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<HitResult> getUserHits(String userId) {
        return hits.stream()
                .filter(hit -> hit.ownerId().equals(userId))
                .toList();
    }

    @Override
    public void clearUserHits(String userId) {
        hits.removeIf(hit -> hit.ownerId().equals(userId));
    }
}
