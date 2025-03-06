package dev.lysmux.lab6.server.collection;

/**
 * Collection metadata class
 *
 * @param type   type of collection
 * @param length size of collection
 * @param path   path to collection file
 */
public record CollectionMeta(String type, int length, String path) {
}
