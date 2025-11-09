package dev.lysmux.tags;

import java.util.Map;

public class Utils {
    public static String buildKey(String clientId, String property) {
        return "%s:%s".formatted(clientId, property);
    }

    public static String buildAjaxScript(String clientId, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("faces.ajax.request(this, event, {'execute': '");
        sb.append(clientId);
        sb.append("', 'render': '");
        sb.append(clientId);
        sb.append("', 'params': {");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("'").append(clientId).append(":")
                    .append(entry.getKey()).append("': '")
                    .append(entry.getValue()).append("', ");
        }
        sb.append("}});return false;");

        return sb.toString();
    }
}
