package org.example.demo.controller;

import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/buscar")
public class BuscaServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        boolean logado = session != null && session.getAttribute("usuarioLogado") != null;

        if (logado) {
            request.setAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
        }

        String termo  = request.getParameter("txtBusca");
        String filtro = request.getParameter("filtro");

        if (termo != null && !termo.trim().isEmpty()) {
            var livros = switch (filtro != null ? filtro : "geral") {
                case "titulo"  -> livroService.buscarPorTitulo(termo);
                case "autor"   -> livroService.buscarPorAutor(termo);
                case "genero"  -> livroService.buscarPorGenero(termo);
                default        -> livroService.buscarGeral(termo);
            };
            request.setAttribute("livros", livros);
        } else {
            request.setAttribute("livros", new ArrayList<>());
        }

        request.setAttribute("termoPesquisado", termo);
        request.setAttribute("filtro", filtro);

        if (logado) {
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/buscar.jsp").forward(request, response);
        }
    }
}