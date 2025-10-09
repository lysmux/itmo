package dev.lysmux.di;

import dev.lysmux.infra.cache.Cache;
import dev.lysmux.infra.cache.LFUCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ApplicationScoped
public class CacheModule {
    @Qualifier
    @Target({ FIELD, PARAMETER, METHOD })
    @Retention(RUNTIME)
    public @interface LFU {}

    @Produces
    @LFU
    Cache.Factory provideCacheFactory() {
        return new LFUCache.Factory();
    }
}
