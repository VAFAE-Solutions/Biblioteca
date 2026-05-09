package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.LivroService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final EmprestimoService emprestimoService = new EmprestimoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        // Proteção por perfil — só ADMIN e BIBLIOTECARIO
        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN &&
                usuarioLogado.getTipo() != Usuario.Tipo.BIBLIOTECARIO) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Livros
        request.setAttribute("livros", livroService.listarTodos());
        request.setAttribute("usuarioLogado", usuarioLogado);

        // ✅ Cards extras para Admin
        if (usuarioLogado.getTipo() == Usuario.Tipo.ADMIN) {
            request.setAttribute("totalUsuarios",
                    usuarioService.listarTodos().size());
            request.setAttribute("totalAtrasados",
                    emprestimoService.buscarAtrasados().size());
        }

        // ✅ Cards extras para Bibliotecário
        if (usuarioLogado.getTipo() == Usuario.Tipo.BIBLIOTECARIO) {
            request.setAttribute("totalAtivos",
                    emprestimoService.buscarAtivos().size());
        }

        request.getRequestDispatcher("/admin_dashboard.jsp")
                .forward(request, response);
    }
}