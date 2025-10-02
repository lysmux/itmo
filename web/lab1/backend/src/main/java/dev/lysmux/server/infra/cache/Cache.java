package dev.lysmux.server.infra.cache;

import java.util.Optional;

public interface Cache<K, V> {
    Optional<V> get(K key);

    void put(K key, V value);

    void remove(K key);

    void clear();

    int size();

    interface Factory {
        <K, V> Cache<K, V> create(int capacity);
    }
}
