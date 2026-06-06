package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        // ✅ Redireciona admin/bibliotecário para o painel correto
        if (usuarioLogado.getTipo() == Usuario.Tipo.ADMIN ||
                usuarioLogado.getTipo() == Usuario.Tipo.BIBLIOTECARIO) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        String txtBusca = request.getParameter("txtBusca");
        String filtro   = request.getParameter("filtro");

        if (txtBusca != null && !txtBusca.trim().isEmpty()) {
            if ("autor".equals(filtro)) {
                request.setAttribute("livros", livroService.buscarPorAutor(txtBusca));
            } else if ("genero".equals(filtro)) {
                request.setAttribute("livros", livroService.buscarPorGenero(txtBusca));
            } else {
                request.setAttribute("livros", livroService.buscarGeral(txtBusca));
            }
        } else {
            request.setAttribute("livros", livroService.listarTodos());
        }

        request.setAttribute("termoPesquisado", txtBusca);
        request.setAttribute("filtroAtivo", filtro);
        request.setAttribute("usuarioLogado", usuarioLogado);

        request.getRequestDispatcher("/dashboard.jsp")
                .forward(request, response);
    }
}
