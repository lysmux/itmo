package dev.lysmux.lab4.service.hit;

import com.fasterxml.uuid.Generators;
import dev.lysmux.lab4.domain.checker.HitChecker;
import dev.lysmux.lab4.domain.model.HitResult;
import dev.lysmux.lab4.domain.repository.HitRepository;
import dev.lysmux.lab4.service.hit.exception.HitNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class HitService {
    @Inject
    private HitRepository hitRepository;

    @Inject
    @Any
    private Instance<HitChecker> checkers;

    public HitResult checkHit(String userId, double x, double y, double r) {
        long startTime = System.nanoTime();
        ZonedDateTime time = ZonedDateTime
                .now(ZoneId.of("Europe/Moscow"))
                .truncatedTo(ChronoUnit.SECONDS);

        boolean hit = checkers.stream()
                .anyMatch(checker -> checker.contains(x, y, r));
        HitResult result = HitResult.builder()
                .id(Generators.timeBasedEpochRandomGenerator().generate().toString())
                .ownerId(userId)
                .x(x)
                .y(y)
                .r(r)
                .hit(hit)
                .time(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .executionTime(System.nanoTime() - startTime)
                .build();
        hitRepository.addHit(result);

        return result;
    }

    public List<HitResult> getUserHits(String userId) {
        return hitRepository.getUserHits(userId);
    }

    public HitResult getHit(String id) {
        HitResult hit = hitRepository.getHit(id);
        if (hit == null) {
            throw new HitNotFoundException(id);
        }

        return hit;
    }

    public void clearUserHits(String userId) {
        hitRepository.clearUserHits(userId);
    }
}
