package org.example.demo.controller;

import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String txtBusca = request.getParameter("txtBusca");

        if (txtBusca != null && !txtBusca.trim().isEmpty()) {
            request.setAttribute("livros", livroService.buscarGeral(txtBusca));
        } else {
            request.setAttribute("livros", new java.util.ArrayList<>());
        }

        // ✅ Passa todos os livros para exibir nas seções
        request.setAttribute("todosLivros", livroService.listarTodos());
        request.setAttribute("termoPesquisado", txtBusca);

        request.getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }
}