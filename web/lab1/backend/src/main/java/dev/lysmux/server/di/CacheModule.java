package dev.lysmux.server.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import dev.lysmux.server.infra.cache.Cache;
import dev.lysmux.server.infra.cache.LFUCache;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class CacheModule extends AbstractModule {
    @Qualifier
    @Target({ FIELD, PARAMETER, METHOD })
    @Retention(RUNTIME)
    public @interface LFU {}

    @Provides
    @LFU
    Cache.Factory provideCacheFactory() {
        return new LFUCache.Factory();
    }
}
