package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.ReservaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/minhas-reservas")
public class MinhasReservasServlet extends HttpServlet {

    private final ReservaService reservaService = new ReservaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            request.setAttribute("reservas",
                    reservaService.buscarPorUsuario(usuarioLogado.getId()));

            request.getRequestDispatcher("/minhas_reservas.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/dashboard?erro=reservas_falhou");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        String acao      = request.getParameter("acao");
        String reservaId = request.getParameter("id");

        try {
            if ("cancelar".equals(acao)) {
                reservaService.cancelarReserva(Integer.parseInt(reservaId));
                response.sendRedirect(request.getContextPath()
                        + "/minhas-reservas?cancelamento=sucesso");
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/minhas-reservas");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/minhas-reservas?cancelamento=erro");
        }
    }
}
