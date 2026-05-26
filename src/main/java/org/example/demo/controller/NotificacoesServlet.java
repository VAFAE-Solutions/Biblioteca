package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.MensagemContatoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/notificacoes")
public class NotificacoesServlet extends HttpServlet {

    private final MensagemContatoService mensagemService = new MensagemContatoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN &&
                usuarioLogado.getTipo() != Usuario.Tipo.BIBLIOTECARIO) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("mensagens", mensagemService.listarTodas());
        request.setAttribute("totalNaoLidas", mensagemService.contarNaoLidas());

        request.getRequestDispatcher("/notificacoes.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        int id      = Integer.parseInt(request.getParameter("id"));

        try {
            if ("marcar_lida".equals(acao)) {
                mensagemService.marcarComoLida(id);
            }
            response.sendRedirect(request.getContextPath()
                    + "/admin/notificacoes?acao=lida");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/notificacoes?acao=erro");
        }
    }
}