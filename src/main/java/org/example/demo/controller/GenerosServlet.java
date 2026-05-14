package org.example.demo.controller;

import org.example.demo.service.LivroService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/generos")
public class GenerosServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("livros", livroService.listarTodos());
        request.getRequestDispatcher("/generos.jsp").forward(request, response);
    }
}
