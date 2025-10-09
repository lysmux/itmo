package dev.lysmux.service;


import dev.lysmux.di.CacheModule;
import dev.lysmux.domain.DotCheck;
import dev.lysmux.domain.checkers.ContainsChecker;
import dev.lysmux.infra.cache.Cache;
import dev.lysmux.infra.repository.DotsRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class DotsService {
    @Inject
    private DotsRepository dotsRepository;

    @Inject
    @Any
    private Instance<ContainsChecker> checkers;

    @Inject
    @CacheModule.LFU
    private Cache.Factory cacheFactory;

    private Cache<String, Boolean> dotsCache;

    @PostConstruct
    public void init() {
        dotsCache = cacheFactory.create(100);
    }

    public DotCheck[] check(double[] xCoords, double yCoord, double[] rCoords) {
        DotCheck[] checks = new DotCheck[xCoords.length * rCoords.length];

        int i = 0;
        for (double x : xCoords) {
            for (double r : rCoords) {
                checks[i] = DotCheck.builder()
                        .x(x)
                        .y(yCoord)
                        .r(r)
                        .contains(checkPoint(x, yCoord, r))
                        .build();
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
        }
        ;

        Optional<DotCheck> dbValue = dotsRepository.get(x, y, r);
        if (dbValue.isPresent()) {
            log.info("Value got from database: %s".formatted(cacheKey));
            dotsCache.put(cacheKey, dbValue.get().isContains());
            return dbValue.get().isContains();
        }

        boolean calculatedValue = checkers.stream()
                .anyMatch(checker -> checker.contains(x, y, r));
        log.info("Value calculated: %s".formatted(cacheKey));

        dotsCache.put(cacheKey, calculatedValue);
        dotsRepository.add(DotCheck.builder()
                .x(x)
                .y(y)
                .r(r)
                .contains(calculatedValue)
                .build());

        return calculatedValue;
    }

    private String buildCacheKey(double x, double y, double r) {
        return "%s:%s:%s".formatted(x, y, r);
    }
}
