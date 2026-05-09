package org.example.demo.controller;

import org.example.demo.model.Exemplar;
import org.example.demo.model.Livro;
import org.example.demo.service.ExemplarService;
import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/detalhes")
public class DetalhesServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();
    private final ExemplarService exemplarService = new ExemplarService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);

                // Busca o livro
                Livro livro = livroService.buscarPorId(id);

                if (livro != null) {
                    request.setAttribute("livro", livro);

                    // ✅ Verifica se há exemplar disponível
                    List<Exemplar> disponiveis = exemplarService
                            .buscarDisponiveisPorLivroEUnidade(id, 1);
                    request.setAttribute("exemplarDisponivel", !disponiveis.isEmpty());

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