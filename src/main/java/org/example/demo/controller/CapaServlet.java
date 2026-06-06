package org.example.demo.controller;

import org.example.demo.dao.LivroDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/capa")
public class CapaServlet extends HttpServlet {

    private final LivroDAO livroDAO = new LivroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            // ✅ Busca só a imagem — sem carregar o livro inteiro
            byte[] imagem = livroDAO.buscarCapaImagem(id);

            if (imagem == null || imagem.length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control", "max-age=86400"); // cache 1 dia
            response.getOutputStream().write(imagem);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
