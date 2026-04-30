package org.example.demo.controller;

import org.example.demo.model.Livro;
import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/detalhes")
public class DetalhesServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Livro livro = livroService.buscarPorId(id);

                if (livro != null) {
                    request.setAttribute("livro", livro);
                    request.getRequestDispatcher("/livro.jsp")
                            .forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}