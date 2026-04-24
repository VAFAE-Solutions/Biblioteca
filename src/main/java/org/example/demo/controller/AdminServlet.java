package org.example.demo.controller;

import org.example.demo.dao.LivroDAO;
import org.example.demo.model.Livro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private LivroDAO livroDAO = new LivroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Map<String, String> usuario = (Map<String, String>) (session != null ? session.getAttribute("usuarioLogado") : null);

        // PROTEÇÃO: Só entra se for ADMIN ou BIBLIOTECARIO
        if (usuario == null || (!"ADMIN".equals(usuario.get("tipo")) && !"BIBLIOTECARIO".equals(usuario.get("tipo")))) {
            response.sendRedirect("login.jsp?erro=acesso_negado");
            return;
        }

        // Carrega os livros para o admin poder gerenciar (editar/excluir)
        List<Livro> lista = livroDAO.listarTodos();
        request.setAttribute("livros", lista);

        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }
}