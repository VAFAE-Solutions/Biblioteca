package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.MultaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservar")
public class EmprestimoServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final MultaService multaService = new MultaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String livroIdParam = request.getParameter("id");

        if (livroIdParam == null || livroIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("/livro.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        String exemplarIdParam = request.getParameter("exemplarId");
        String livroIdParam    = request.getParameter("livroId");

        try {
            int exemplarId = Integer.parseInt(exemplarIdParam);
            int livroId    = Integer.parseInt(livroIdParam);

            if (multaService.usuarioPossuiMultaPendente(usuarioLogado.getId())) {
                response.sendRedirect(request.getContextPath()
                        + "/detalhes?id=" + livroId + "&erro=multa_pendente");
                return;
            }

            // ✅ ALTERADO — passa o objeto inteiro em vez de só o ID
            emprestimoService.realizarEmprestimo(exemplarId, usuarioLogado);
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroId + "&reserva=sucesso");

        } catch (IllegalStateException e) {
            // ✅ ALTERADO — distingue erro de limite dos outros erros
            String motivo = e.getMessage().contains("Limite") ? "limite_atingido" : "indisponivel";
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroIdParam + "&reserva=erro&motivo=" + motivo);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/dashboard?erro=dados_invalidos");
        }
    }
}