package dev.lysmux.server.servlet;

import dev.lysmux.server.ResponseUtils;
import dev.lysmux.server.dto.CheckResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@WebServlet("/history")
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        @SuppressWarnings("unchecked")
        Optional<List<CheckResult>> itemList = Optional.ofNullable((List<CheckResult>) getServletContext().getAttribute("checks"));

        ResponseUtils.setJsonResponse(resp, itemList.orElse(List.of()));
    }
}
