package dev.lysmux.servlet;

import com.google.gson.Gson;
import dev.lysmux.domain.DotCheck;
import dev.lysmux.dto.CheckRequest;
import dev.lysmux.dto.CheckResult;
import dev.lysmux.parser.ObjectParser;
import dev.lysmux.service.DotsService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Log
@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    @Inject
    private DotsService dotsService;

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.nanoTime();
        ZonedDateTime time = ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS);

        CheckRequest checkRequest = ObjectParser.parse(req.getParameterMap(), CheckRequest.class);

        DotCheck[] checks = dotsService.check(checkRequest.x(), checkRequest.y(), checkRequest.r());

        CheckResult response = CheckResult.builder()
                .checks(checks)
                .time(time.format(DateTimeFormatter.ISO_INSTANT))
                .executionTime(System.nanoTime() - startTime)
                .build();

        ServletContext context = getServletContext();
        synchronized (this) {
            @SuppressWarnings("unchecked")
            List<CheckResult> itemList = (List<CheckResult>) context.getAttribute("checks");
            if (itemList == null) {
                itemList = new ArrayList<>();
            }

            itemList.add(response);
            context.setAttribute("checks", itemList);
        }

        log.info(context.getAttribute("checks").toString());

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(gson.toJson(response));
    }
}
