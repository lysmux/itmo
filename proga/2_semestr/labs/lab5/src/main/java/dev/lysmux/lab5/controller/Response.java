package dev.lysmux.lab5.controller;

import dev.lysmux.lab5.collection.model.LabWork;

import java.util.Collection;

/**
 * Class that stores response information
 *
 * @param text     response text
 * @param entities response entities
 * @since 1.0
 */
public record Response(String text, Collection<LabWork> entities) {
    /**
     * Generates response with text and entities
     *
     * @param text     response text
     * @param entities response entities
     * @return generated {@link Response}
     */
    public static Response of(String text, Collection<LabWork> entities) {
        return new Response(text, entities);
    }

    /**
     * Generates response with only text
     *
     * @param text response text
     * @return generated {@link Response}
     */
    public static Response of(String text) {
        return new Response(text, null);
    }

    /**
     * Generated empty response
     *
     * @return generated {@link Response}
     */
    public static Response empty() {
        return new Response(null, null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (text != null) sb.append(text);
        if (entities != null) {
            for (var entity : entities) {
                sb.append("\n").append(entity);
            }
        }

        return sb.toString();
    }
}
