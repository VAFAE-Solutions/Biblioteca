package org.example.demo.controller;

import org.example.demo.service.UnidadeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/localizacao")
public class LocalizacaoServlet extends HttpServlet {

    private final UnidadeService unidadeService = new UnidadeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("unidades", unidadeService.listarTodas());
        request.getRequestDispatcher("/localizacao.jsp").forward(request, response);
    }
}