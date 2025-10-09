package dev.lysmux.server.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.ZonedDateTime;

public class JsonSerializer{
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new DateTimeConverter())
            .create();

    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz){
        return gson.fromJson(json, clazz);
    }
}
