package org.example.demo.controller;

import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String txtBusca = request.getParameter("txtBusca");

        // Só busca se houver termo — rota pública não carrega tudo por padrão
        if (txtBusca != null && !txtBusca.trim().isEmpty()) {
            request.setAttribute("livros", livroService.buscarGeral(txtBusca));
        } else {
            request.setAttribute("livros", new ArrayList<>());
        }

        request.setAttribute("termoPesquisado", txtBusca);

        request.getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }
}