package dev.lysmux.server.servlet;

import dev.lysmux.server.ResponseUtils;
import dev.lysmux.server.dto.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@WebServlet("/clear")
public class ClearResultsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().setAttribute("checks", new ArrayList<>());

        log.info("Results cleared");

        ResponseUtils.setJsonResponse(resp, new Response("results cleared"));
    }
}
