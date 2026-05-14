package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/excluir-livro")
public class ExcluirLivroServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            livroService.desativar(id); // ✅ desativar em vez de deletar
            response.sendRedirect(request.getContextPath()
                    + "/admin?exclusao=sucesso");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin?exclusao=erro");
        }
    }
}
