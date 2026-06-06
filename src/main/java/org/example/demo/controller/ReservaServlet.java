package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.ReservaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservar-livro")
public class ReservaServlet extends HttpServlet {

    private final ReservaService reservaService = new ReservaService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        String livroIdParam = request.getParameter("livroId");

        try {
            int livroId = Integer.parseInt(livroIdParam);

            reservaService.realizarReserva(
                    livroId,
                    usuarioLogado.getId(),
                    2 // unidade padrão por enquanto
            );

            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroId + "&reserva=sucesso_reserva");

        } catch (IllegalStateException e) {
            String mensagem = e.getMessage().contains("multa")
                    ? "multa_pendente" : "reserva_erro";
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroIdParam + "&erro=" + mensagem);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroIdParam + "&erro=reserva_erro");
        }
    }
}
