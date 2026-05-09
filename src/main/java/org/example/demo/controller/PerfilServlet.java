package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioEstudante;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.MultaService;
import org.example.demo.service.ReservaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final MultaService multaService = new MultaService();
    private final ReservaService reservaService = new ReservaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            int id = usuarioLogado.getId();

            // Total de empréstimos ativos
            long totalEmprestimos = emprestimoService
                    .buscarPorUsuario(id)
                    .stream()
                    .filter(e -> e.getStatus().name().equals("ATIVO")
                            || e.getStatus().name().equals("ATRASADO"))
                    .count();

            // Total de multas pendentes
            double totalMulta = multaService
                    .calcularTotalMultasPendentes(id);

            // Total de reservas ativas
            long totalReservas = reservaService
                    .buscarPorUsuario(id)
                    .stream()
                    .filter(r -> r.getStatus().name().equals("AGUARDANDO"))
                    .count();

            request.setAttribute("totalEmprestimos", totalEmprestimos);
            request.setAttribute("totalMulta", totalMulta);
            request.setAttribute("totalReservas", totalReservas);

            // ✅ Cast para UsuarioEstudante para exibir RA no perfil
            if (usuarioLogado instanceof UsuarioEstudante estudante) {
                request.setAttribute("ra", estudante.getRa());
            }

            request.getRequestDispatcher("/perfil_usuario.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/dashboard?erro=perfil_falhou");
        }
    }
}