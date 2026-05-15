package org.example.demo.controller;

import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/esqueceu-senha")
public class EsqueceuSenhaServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/esqueceu_senha.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        try {
            usuarioService.solicitarReset(email);
            response.sendRedirect(request.getContextPath()
                    + "/esqueceu-senha?sucesso=true");
        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/esqueceu_senha.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao processar solicitação. Tente novamente.");
            request.getRequestDispatcher("/esqueceu_senha.jsp")
                    .forward(request, response);
        }
    }
}