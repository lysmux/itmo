package dev.lysmux.servlet;

import com.google.gson.Gson;
import dev.lysmux.dto.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.ArrayList;

@Log
@WebServlet("/clear")
public class ClearResultsServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().setAttribute("checks", new ArrayList<>());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(gson.toJson(new Response("results cleared")));
    }
}
