package dev.lysmux.lab8.common.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class that stores response information
 *
 * @param text    response text
 * @param objects response objects
 * @since 1.0
 */
public record Response(
        String text,
        List<?> objects,
        boolean success
) implements Serializable {
    public static class ResponseBuilder {
        private String text;
        private List<?> objects;
        private boolean success = true;

        public ResponseBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ResponseBuilder objects(List<?> objects) {
            this.objects = objects;
            return this;
        }

        public ResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public Response build() {
            return new Response(text, objects, success);
        }
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (text != null) sb.append(text);
        if (objects != null) {
            for (var object : objects) {
                sb.append("\n").append(object);
            }
        }

        return sb.toString();
    }
}
