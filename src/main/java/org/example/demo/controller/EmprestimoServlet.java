package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.MultaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservar")
public class EmprestimoServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final MultaService multaService = new MultaService();

    // Exibe confirmação antes de reservar
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

    // Realiza o empréstimo
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        String exemplarIdParam = request.getParameter("exemplarId");
        String livroIdParam    = request.getParameter("livroId");

        try {
            int exemplarId = Integer.parseInt(exemplarIdParam);
            int livroId    = Integer.parseInt(livroIdParam);

            // Verifica multa pendente
            if (multaService.usuarioPossuiMultaPendente(usuarioLogado.getId())) {
                response.sendRedirect(request.getContextPath()
                        + "/detalhes?id=" + livroId + "&erro=multa_pendente");
                return;
            }

            emprestimoService.realizarEmprestimo(exemplarId, usuarioLogado.getId());
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroId + "&reserva=sucesso");

        } catch (IllegalStateException e) {
            response.sendRedirect(request.getContextPath()
                    + "/detalhes?id=" + livroIdParam + "&reserva=erro");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/dashboard?erro=dados_invalidos");
        }
    }
}