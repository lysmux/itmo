package dev.lysmux.fcgi.dto;

import com.fastcgi.FCGIInterface;
import dev.lysmux.fcgi.enums.HTTPMethod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public record Request(
        String fromHost,
        String serverName,
        HTTPMethod method,
        String path,
        Map<String, String> headers,
        String queryParams,
        String body
) {
    public static Request fromFCGI() throws IOException {
        Properties properties = FCGIInterface.request.params;

        Map<String, String> headers = parseHeaders(properties);

        String requestUri = properties.getProperty("REQUEST_URI");
        String scriptName = properties.getProperty("SCRIPT_NAME");
        String url = "/" + requestUri.substring(scriptName.length());

        FCGIInterface.request.inStream.fill();
        int contentLength = FCGIInterface.request.inStream.available();
        byte[] buffer = new byte[contentLength];
        FCGIInterface.request.inStream.read(buffer);
        String body = new String(buffer, StandardCharsets.UTF_8);


        return new Request(
                properties.getProperty("REMOTE_ADDR"),
                properties.getProperty("SERVER_NAME"),
                HTTPMethod.valueOf(properties.getProperty("REQUEST_METHOD")),
                url.split("\\?")[0],
                headers,
                properties.getProperty("QUERY_STRING"),
                body
        );
    }

    private static Map<String, String> parseHeaders(Properties properties) {
        return properties.entrySet().stream()
                .filter((entry) -> entry.getKey().toString().startsWith("HTTP_"))
                .collect(Collectors.toMap(
                        el -> el.getKey().toString().substring(5),
                        el -> el.getValue().toString()
                ));
    }
}
