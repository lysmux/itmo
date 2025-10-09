package dev.lysmux.tags;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    public static String generateHex() {
        int randomInt = random.nextInt();
        return Integer.toHexString(randomInt);
    }

    public static void loadJS(String path, JspWriter out) throws JspException, IOException {
        out.println("<script>");
        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new JspException("'%s' script not found".formatted(path));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            )) {
                String line;
                while ((line = reader.readLine()) != null) {
                    out.println(line);
                }
            }
        }
        out.println("</script>");
    }
}
