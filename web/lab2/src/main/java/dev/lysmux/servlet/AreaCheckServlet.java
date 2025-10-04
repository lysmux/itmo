package dev.lysmux.servlet;

import com.google.gson.Gson;
import dev.lysmux.ValidationException;
import dev.lysmux.dto.CheckRequest;
import dev.lysmux.parser.ArgParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Log
@WebServlet(name = "AreaCheckServlet", urlPatterns = "/check")
public class AreaCheckServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final ArgParser argParser = new ArgParser();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CheckRequest checkRequest = parseRequest(req.getParameterMap());

//        CheckResponse response = CheckResponse.builder()
//                .x(x)
//                .y(y)
//                .r(r)
//                .build();
//
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(gson.toJson(checkRequest));
    }

    private CheckRequest parseRequest(Map<String, String[]> parameter) {
        double x = argParser.parse(parameter.get("x")[0], double.class);
        double y = argParser.parse(parameter.get("y")[0], double.class);
        double r = argParser.parse(parameter.get("r")[0], double.class);

        CheckRequest cr = new CheckRequest(x, y, r);

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<CheckRequest>> violations =vf.getValidator().validate(cr);

        if (!violations.isEmpty()) {
            throw ValidationException.fromConstraintViolation(violations);
        }

        return cr;
    }
}
