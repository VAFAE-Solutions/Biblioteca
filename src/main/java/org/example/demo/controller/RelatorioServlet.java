package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.RelatorioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/relatorios")
public class RelatorioServlet extends HttpServlet {

    private final RelatorioService relatorioService = new RelatorioService();

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
        request.setAttribute("livrosMaisEmprestados",
                relatorioService.livrosMaisEmprestados());
        request.setAttribute("taxaAtraso",
                relatorioService.taxaAtraso());
        request.setAttribute("emprestimosPorUsuario",
                relatorioService.emprestimosPorUsuario());
        request.setAttribute("livrosAtrasados",
                relatorioService.livrosAtrasados());
        request.setAttribute("resumoMultas", relatorioService.resumoMultas());

        request.getRequestDispatcher("/relatorios.jsp")
                .forward(request, response);
    }
}
