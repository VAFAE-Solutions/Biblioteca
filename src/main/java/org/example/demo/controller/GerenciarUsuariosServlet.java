package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.MultaService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/usuarios")
public class GerenciarUsuariosServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final MultaService multaService = new MultaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // ✅ Se vier um id, carrega detalhes do usuário para o modal
        String idParam = request.getParameter("detalhe");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Usuario usuarioDetalhe = usuarioService.buscarPorId(id);
                if (usuarioDetalhe != null) {
                    request.setAttribute("usuarioDetalhe", usuarioDetalhe);
                    request.setAttribute("multasUsuario",
                            multaService.buscarPorUsuario(id));
                    request.setAttribute("totalMulta",
                            multaService.calcularTotalMultasPendentes(id));
                }
            } catch (NumberFormatException ignored) {}
        }

        request.setAttribute("usuarioLogado", usuarioLogado);
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
                case "bloquear"         -> usuarioService.bloquear(id);
                case "desbloquear"      -> usuarioService.desbloquear(id);
                case "desativar"        -> usuarioService.desativar(id);
                case "reativar"         -> usuarioService.reativar(id);
                case "ajustar_limite"   -> {
                    // ✅ Ajusta limite customizado
                    String limiteParam = request.getParameter("limite");
                    if (limiteParam != null && !limiteParam.isBlank()) {
                        usuarioService.ajustarLimiteCotas(id, Integer.parseInt(limiteParam));
                    }
                }
                case "resetar_limite"   -> {
                    // ✅ Reseta para o padrão do tipo
                    usuarioService.resetarLimiteCotas(id);
                }
            }
            response.sendRedirect(request.getContextPath()
                    + "/admin/usuarios?acao=" + acao);
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/usuarios?acao=erro&msg=" + e.getMessage());
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/usuarios?acao=erro");
        }
    }
}
