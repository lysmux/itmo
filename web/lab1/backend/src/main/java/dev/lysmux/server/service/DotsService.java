package dev.lysmux.server.service;

import com.google.inject.Inject;
import dev.lysmux.server.di.CacheModule;
import dev.lysmux.server.domain.DotCheck;
import dev.lysmux.server.domain.checkers.ContainsChecker;
import dev.lysmux.server.infra.cache.Cache;
import dev.lysmux.server.infra.repository.DotsRepository;
import lombok.extern.java.Log;

import java.util.Optional;
import java.util.Set;

@Log
public class DotsService {
    private final DotsRepository dotsRepository;
    private final Set<ContainsChecker> checkers;

    private final Cache<String, Boolean> dotsCache;

    @Inject
    public DotsService(
            @CacheModule.LFU Cache.Factory cacheFactory,
            DotsRepository repository,
            Set<ContainsChecker> checkers
    ) {
        this.dotsCache = cacheFactory.create(100);
        this.dotsRepository = repository;
        this.checkers = checkers;
    }

    public DotCheck[] check(double[] xCoords, double yCoord, double[] rCoords) {
        DotCheck[] checks = new DotCheck[xCoords.length * rCoords.length];

        int i = 0;
        for (double x : xCoords) {
            for (double r : rCoords) {
                checks[i] = new DotCheck(x, yCoord, r, checkPoint(x, yCoord, r));
                i++;
            }
        }

        return checks;
    }

    private boolean checkPoint(double x, double y, double r) {
        String cacheKey = buildCacheKey(x, y, r);

        Optional<Boolean> cachedValue = dotsCache.get(cacheKey);
        if (cachedValue.isPresent()) {
            log.info("Value got from cache: %s".formatted(cacheKey));
            return cachedValue.get();
        };

        Optional<DotCheck> dbValue = dotsRepository.get(x, y, r);
        if (dbValue.isPresent()) {
            log.info("Value got from database: %s".formatted(cacheKey));
            dotsCache.put(cacheKey, dbValue.get().contains());
            return dbValue.get().contains();
        }

        boolean calculatedValue = checkers.stream()
                .anyMatch(checker -> checker.contains(x, y, r));
        log.info("Value calculated: %s".formatted(cacheKey));

        dotsCache.put(cacheKey, calculatedValue);
        dotsRepository.add(new DotCheck(x, y, r, calculatedValue));

        return calculatedValue;
    }

    private String buildCacheKey(double x, double y, double r) {
        return "%s:%s:%s".formatted(x, y, r);
    }
}
