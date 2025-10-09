package dev.lysmux.infra.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LFUCache<K, V> implements Cache<K, V> {
    @Getter
    private final int capacity;

    private final Map<K, V> cache = new HashMap<>();

    @Getter
    private final Map<K, Integer> freq = new LinkedHashMap<>();

    public LFUCache(int capacity) {
        this.capacity = capacity;
    }

    public Optional<V> get(K key) {
        if (cache.containsKey(key)) {
            log.info("Cache hit: {}", key);

            freq.put(key, freq.get(key) + 1);
            return Optional.ofNullable(cache.get(key));
        }

        log.info("Cache miss: {}", key);

        return Optional.empty();
    }

    public void put(K key, V value) {
        if (cache.size() >= capacity) {
            freeCache();
        }

        cache.put(key, value);
        freq.put(key, 1);

        log.info("Put to cache: {}", key);
    }

    public void remove(K key) {
        cache.remove(key);
        freq.remove(key);

        log.info("Removed from cache: {}", key);
    }

    @Override
    public void clear() {
        cache.clear();
        freq.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    private void freeCache() {
        int minFreq = freq.values().stream().mapToInt(i -> i).min().orElse(0);
        freq.entrySet().stream()
                .filter(e -> e.getValue() == minFreq)
                .findFirst().ifPresent(e -> {
                    remove(e.getKey());
                    log.info("Cache free: {}. Freq: {}", e.getKey(), minFreq);
                });
    }

    public static class Factory implements Cache.Factory {
        @Override
        public <K, V> Cache<K, V> create(int capacity) {
            return new LFUCache<>(capacity);
        }
    }
}
