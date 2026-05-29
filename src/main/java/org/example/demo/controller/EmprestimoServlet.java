package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.ExemplarService;
import org.example.demo.model.Exemplar;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/reservar")
public class EmprestimoServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final ExemplarService exemplarService = new ExemplarService();

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

        String acao = request.getParameter("acao");

        // ✅ Devolução
        if ("devolver".equals(acao)) {
            try {
                int emprestimoId = Integer.parseInt(request.getParameter("id"));
                emprestimoService.finalizarEmprestimo(emprestimoId);
                response.sendRedirect(request.getContextPath()
                        + "/meus-emprestimos?devolucao=sucesso");
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath()
                        + "/meus-emprestimos?devolucao=erro");
            }
            return;
        }

        // ✅ Empréstimo
        String livroIdParam = request.getParameter("livroId");

        try {
            int livroId = Integer.parseInt(livroIdParam);

            List<Exemplar> disponiveis = exemplarService
                    .buscarDisponiveisPorLivroEUnidade(livroId, 2);

            if (disponiveis.isEmpty()) {
                response.sendRedirect(request.getContextPath()
                        + "/detalhes?id=" + livroId + "&reserva=erro");
                return;
            }

            int exemplarId = disponiveis.get(0).getId();
            emprestimoService.realizarEmprestimo(exemplarId, usuarioLogado.getId());
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroId + "&reserva=sucesso");

        } catch (IllegalStateException e) {
            String mensagem = e.getMessage().contains("multa")
                    ? "multa_pendente" : "limite_atingido";
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroIdParam + "&erro=" + mensagem);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/dashboard?erro=dados_invalidos");
        }
    }
}