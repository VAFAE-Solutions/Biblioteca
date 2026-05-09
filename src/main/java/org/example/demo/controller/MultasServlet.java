package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.MultaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/multas")
public class MultasServlet extends HttpServlet {

    private final MultaService multaService = new MultaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            int id = usuarioLogado.getId();

            request.setAttribute("multas",
                    multaService.buscarPorUsuario(id));

            request.setAttribute("totalMulta",
                    multaService.calcularTotalMultasPendentes(id));

            request.getRequestDispatcher("/multas.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/dashboard?erro=multas_falhou");
        }
    }
}