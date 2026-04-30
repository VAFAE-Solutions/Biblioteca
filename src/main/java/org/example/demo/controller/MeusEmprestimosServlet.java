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
            // Lista empréstimos do usuário logado
            request.setAttribute("emprestimos",
                    emprestimoService.buscarPorUsuario(usuarioLogado.getId()));

            // Calcula total de multas pendentes
            request.setAttribute("totalMulta",
                    multaService.calcularTotalMultasPendentes(usuarioLogado.getId()));

            request.getRequestDispatcher("/meus_emprestimos.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/dashboard?erro=lista_falhou");
        }
    }
}