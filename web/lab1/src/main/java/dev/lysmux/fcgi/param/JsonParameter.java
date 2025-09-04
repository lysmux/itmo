package dev.lysmux.fcgi.param;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonParameter implements MapParameter<JsonObject>{
    private final JsonObject value;

    public JsonParameter(JsonObject value) {
        this.value = value;
    }

    @Override
    public JsonObject get() {
        return value;
    }

    public Parameter<?> get(String key) {
        JsonElement element = value.get(key);
        if (element == null) return null;

        return elementToParameter(element);
    }

    public static Parameter<?> elementToParameter(JsonElement element) {
        if (element.isJsonPrimitive()) return new SimpleParameter(element.getAsString());
        if (element.isJsonObject()) return new JsonParameter(element.getAsJsonObject());

        return element.getAsJsonArray().asList().stream()
                .reduce(new ArrayParameter(),
                        (arr, el) -> {
                            arr.add(elementToParameter(el));
                            return arr;
                        },
                        (arr1, arr2) -> {
                            arr1.addAll(arr2);
                            return arr1;
                        }
                );
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
