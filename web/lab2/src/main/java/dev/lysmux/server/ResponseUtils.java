package dev.lysmux.server;

import dev.lysmux.server.serializer.JsonSerializer;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtils {
    public static void setJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(JsonSerializer.toJson(data));
    }
}
