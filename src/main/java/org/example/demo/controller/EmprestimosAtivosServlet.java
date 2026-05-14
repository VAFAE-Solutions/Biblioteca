package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/emprestimos")
public class EmprestimosAtivosServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();

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

        // Atualiza atrasos automaticamente
        emprestimoService.verificarEAtualizarAtrasos();

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("emprestimosAtivos",
                emprestimoService.buscarAtivos());
        request.setAttribute("emprestimosAtrasados",
                emprestimoService.buscarAtrasados());

        request.getRequestDispatcher("/emprestimos_ativos.jsp")
                .forward(request, response);
    }
}