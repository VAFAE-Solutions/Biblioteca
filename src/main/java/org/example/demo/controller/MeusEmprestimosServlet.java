package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.MultaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/meus-emprestimos")
public class MeusEmprestimosServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final MultaService multaService = new MultaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            emprestimoService.verificarEAtualizarAtrasos();

            var emprestimos = emprestimoService.buscarPorUsuario(usuarioLogado.getId());

            boolean temAtrasado = emprestimos.stream()
                    .anyMatch(e -> e.getStatus().name().equals("ATRASADO"));

            request.setAttribute("usuarioLogado", usuarioLogado);
            request.setAttribute("emprestimos", emprestimos);
            request.setAttribute("totalMulta",
                    multaService.calcularTotalMultasPendentes(usuarioLogado.getId()));
            request.setAttribute("temAtrasado", temAtrasado);

            request.getRequestDispatcher("/meus_emprestimos.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/dashboard?erro=lista_falhou");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int emprestimoId = Integer.parseInt(request.getParameter("id"));
            emprestimoService.finalizarEmprestimo(emprestimoId);
            response.sendRedirect(request.getContextPath()
                    + "/meus-emprestimos?devolucao=sucesso");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/meus-emprestimos?devolucao=erro");
        }
    }
}
