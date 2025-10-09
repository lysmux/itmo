package dev.lysmux.server.servlet;

import dev.lysmux.domain.DotCheck;
import dev.lysmux.server.ResponseUtils;
import dev.lysmux.server.dto.CheckRequest;
import dev.lysmux.server.dto.CheckResult;
import dev.lysmux.server.parser.ObjectParser;
import dev.lysmux.service.DotsService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    @Inject
    private DotsService dotsService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.nanoTime();
        ZonedDateTime time = ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS);

        CheckRequest checkRequest = ObjectParser.parse(req.getParameterMap(), CheckRequest.class);
        DotCheck[] checks = dotsService.check(checkRequest.x(), checkRequest.y(), checkRequest.r());

        List<CheckResult> response = Arrays.stream(checks).map(check -> {
            return CheckResult.builder()
                    .x(check.getX())
                    .y(check.getY())
                    .r(check.getR())
                    .contains(check.isContains())
                    .time(time)
                    .executionTime(System.nanoTime() - startTime)
                    .build();
        }).toList();

        ServletContext context = getServletContext();
        synchronized (this) {
            @SuppressWarnings("unchecked")
            List<CheckResult> itemList = (List<CheckResult>) context.getAttribute("checks");
            if (itemList == null) {
                itemList = new ArrayList<>();
            }

            itemList.addAll(response);
            context.setAttribute("checks", itemList);
        }

        ResponseUtils.setJsonResponse(resp, response);
    }
}
