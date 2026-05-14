package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/usuarios")
public class GerenciarUsuariosServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("usuarioLogado", usuarioLogado);
        // ✅ Admin vê todos incluindo inativos
        request.setAttribute("usuarios", usuarioService.listarTodosIncluindoInativos());

        request.getRequestDispatcher("/gerenciar_usuarios.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            switch (acao) {
                case "bloquear"    -> usuarioService.bloquear(id);
                case "desbloquear" -> usuarioService.desbloquear(id);
                case "desativar"   -> usuarioService.desativar(id); // ✅ desativar em vez de deletar
                case "reativar"    -> usuarioService.reativar(id);  // ✅ reativar
            }
            response.sendRedirect(request.getContextPath()
                    + "/admin/usuarios?acao=" + acao);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/usuarios?acao=erro");
        }
    }
}
