package org.example.demo.controller;

import org.example.demo.model.Unidade;
import org.example.demo.model.Usuario;
import org.example.demo.service.UnidadeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/unidades")
public class GerenciarUnidadesServlet extends HttpServlet {

    private final UnidadeService unidadeService = new UnidadeService();

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
        request.setAttribute("unidades", unidadeService.listarTodas());
        request.getRequestDispatcher("/gerenciar_unidades.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            switch (acao) {
                case "cadastrar" -> {
                    // ✅ Verifica se já existe uma unidade ativa
                    long totalAtivas = unidadeService.listarTodas().size();
                    if (totalAtivas >= 1) {
                        boolean confirmado = "true".equals(request.getParameter("confirmado"));
                        if (!confirmado) {
                            response.sendRedirect(request.getContextPath()
                                    + "/admin/unidades?aviso=uma_unidade");
                            return;
                        }
                    }
                    Unidade unidade = new Unidade(
                            request.getParameter("nome"),
                            request.getParameter("endereco"),
                            request.getParameter("telefone"),
                            request.getParameter("horario")
                    );
                    unidadeService.cadastrar(unidade);
                    response.sendRedirect(request.getContextPath()
                            + "/admin/unidades?acao=cadastrado");
                }
                case "atualizar" -> {
                    Unidade unidade = new Unidade(
                            Integer.parseInt(request.getParameter("id")),
                            request.getParameter("nome"),
                            request.getParameter("endereco"),
                            request.getParameter("telefone"),
                            request.getParameter("horario")
                    );
                    unidadeService.atualizar(unidade);
                    response.sendRedirect(request.getContextPath()
                            + "/admin/unidades?acao=atualizado");
                }
                case "desativar" -> {
                    // ✅ Desativar em vez de deletar
                    int id = Integer.parseInt(request.getParameter("id"));
                    unidadeService.desativar(id);
                    response.sendRedirect(request.getContextPath()
                            + "/admin/unidades?acao=desativado");
                }
                default -> response.sendRedirect(request.getContextPath()
                        + "/admin/unidades");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/unidades?erro=" + e.getMessage());
        }
    }
}
