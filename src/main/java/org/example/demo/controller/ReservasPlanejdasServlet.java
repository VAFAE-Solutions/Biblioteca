package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioBibliotecario;
import org.example.demo.service.ReservaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/reservas")
public class ReservasPlanejdasServlet extends HttpServlet {

    private final ReservaService reservaService = new ReservaService();

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

        int unidadeId = 1;
        if (usuarioLogado instanceof UsuarioBibliotecario bib) {
            unidadeId = bib.getUnidadeId();
        }

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("reservas",
                reservaService.buscarFilaCompleta(unidadeId));

        request.getRequestDispatcher("/reservas_planejadas.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao     = request.getParameter("acao");
        String reservaId = request.getParameter("id");

        try {
            if ("cancelar".equals(acao)) {
                reservaService.cancelarReserva(Integer.parseInt(reservaId));
                response.sendRedirect(request.getContextPath()
                        + "/admin/reservas?acao=cancelado");
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/admin/reservas");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/reservas?acao=erro");
        }
    }
}