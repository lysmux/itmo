package dev.lysmux.server.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/home")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String x = req.getParameter("x");
        String y = req.getParameter("y");
        String r = req.getParameter("r");

        if (x != null && y != null && r != null) {
            log.info("Checking area: ({}, {}, {})", x, y, r);
            getServletContext().getRequestDispatcher("/check").forward(req, resp);
        } else {
            log.info("Rendering home page");
            getServletContext().getRequestDispatcher("/views/home.jsp").forward(req, resp);
        }
    }
}
